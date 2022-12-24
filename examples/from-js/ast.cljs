(ns ast
  "Script that prints full ast of given graph. If given optional node-type
  argument, filters the ast to just print values of that node-type"
  (:require [clojure.pprint :as pprint]
            [clojure.walk :as walk]
            [nbb.core :as nbb]
            [logseq.graph-parser.cli :as gp-cli]))

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
  "Map of node types that user can specify and fns that fetch that node type"
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
       (mapcat :ast)))

(defn- print-ast
  [args]
  (if-not (<= 1 (count args) 2)
    (println "Usage: $0 GRAPH-DIR [NODE-TYPE]")
    (let [[graph-dir node-type] args
          keep-node (when node-type
                      (or (get node-types node-type)
                          (throw (ex-info (str "Invalid node-type. Valid node-types are " (keys node-types))
                                          {}))))
          {:keys [asts]} (gp-cli/parse-graph graph-dir {:verbose false})]
      (if node-type
        (filter-ast asts keep-node)
        asts))))

(defn cljs-print-ast
  [args]
  (pprint/pprint (print-ast args)))

(defn js-print-ast
  [args]
  (clj->js (print-ast (js->clj args))))

;; Only invoke when calling from commandline with nbb-logseq
(when (and *command-line-args* (= nbb/*file* (:file (meta #'cljs-print-ast))))
  (cljs-print-ast *command-line-args*))

#js {:printAst js-print-ast}
