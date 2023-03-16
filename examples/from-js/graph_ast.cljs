(ns graph-ast
  "Script that prints full ast of given graph. If given optional node-type
  argument, filters the ast to just print values of that node-type"
  (:require [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [logseq.graph-parser.cli :as gp-cli]
            [babashka.cli :as cli]
            ["fs" :as fs]))

(defn- url-node
  [node]
  (when (and (= "Link" (first node))
             (= "Complex" (first (:url (second node)))))
    (let [{:keys [protocol link]}
          (second (:url (second node)))]
      (str protocol "://" link))))

(defn- simple-query-node
  [node]
  (when (and (= "Heading" (first node))
             (= "query" (-> node second :title first second :name)))
    (-> node second :title first second :arguments first)))

(defn- advanced-query-node
  [node]
  (when (= ["Custom" "query"] (take 2 node))
    (get node 4)))

(def node-types
  "Map of node types that user can specify and fns that fetch that node type.
The fetching fn returns a non nil value when it finds a matching node. The returned
value isn't usually the full node but rather a concise, practical representation of
it"
  {"simple-query" simple-query-node
   "advanced-query" advanced-query-node
   "url" url-node})

(defn- filter-ast
  [asts keep-node-fn]
  (->> asts
       (mapv (fn [m]
               (update m :ast
                       (fn [nodes]
                         (let [found (atom [])]
                           (walk/postwalk
                            (fn [elem]
                              (when-let [saveable-val (and (vector? elem) (keep-node-fn elem))]
                                (swap! found conj saveable-val))
                              elem)
                            nodes)
                           @found)))))
       (mapcat :ast)
       vec))

(def spec
  "CLI options"
  {:help {:alias :h
          :coerce :boolean
          :desc "Print help"}
   :out-file  {:alias :o
               :coerce :string
               :desc "File to save output"}
   :node-type {:alias :n
               :coerce :string
               :desc "Node type to filter asts"
               :validate (set (keys node-types))}})

(defn -main
  [& args]
  (let [{:keys [out-file help node-type]}
        (cli/parse-opts args {:spec spec
                              :error-fn (fn [{:keys [msg type option] :as data}]
                                          (if (= :org.babashka/cli type)
                                            (println "Error:"
                                                     (str msg
                                                          (when (= :node-type option)
                                                            (str ". Valid node-types are " (string/join ", " (keys node-types))))))
                                            (throw (ex-info msg data)))
                                          (js/process.exit 1))})]
    (if help
      (println (str "Usage: $0 [GRAPH-DIR] [OPTIONS]\nOptions:\n"
                    (cli/format-opts {:spec spec})
                    "\nValid node-types are " (string/join ", " (keys node-types))))
      (let [graph-dir (or (first args) ".")
            keep-node (get node-types node-type)
            {:keys [asts]} (gp-cli/parse-graph graph-dir {:verbose false})
            final-asts (if node-type (filter-ast asts keep-node) asts)]
       (if out-file
         (fs/writeFileSync out-file (with-out-str (pprint/pprint final-asts)))
         (pprint/pprint final-asts))))))

#js {:main -main}
