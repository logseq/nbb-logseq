(ns logseq.ollama-chat
  (:require [babashka.cli :as cli]
            [cljs.pprint :as pprint]
            [clojure.string :as string]
            [datascript.core :as d]
            #_:clj-kondo/ignore
            [logseq.db.sqlite.cli :as sqlite-cli]
            [promesa.core :as p]
            ["path" :as node-path]
            ["os" :as os]))

(def user-config
  {:schema.class/Movie [:schema.property/actor
                        :schema.property/director
                        :schema.property/musicBy
                        :schema.property/datePublished
                        :schema.property/url]})

(defn- get-dir-and-db-name
  "Gets dir and db name for use with open-db!"
  [graph-dir]
  (if (string/includes? graph-dir "/")
    (let [graph-dir'
          (node-path/join (or js/process.env.ORIGINAL_PWD ".") graph-dir)]
      ((juxt node-path/dirname node-path/basename) graph-dir'))
    [(node-path/join (os/homedir) "logseq" "graphs") graph-dir]))

(defn- ->chat-properties [graph-properties]
  (merge {:name {:type :string}}
         (->> graph-properties
              (map (fn [[k v]]
                     (let [chat-property
                           (case (get-in graph-properties [k :logseq.property/type])
                             :node
                             {:type :object
                              :properties
                              {:name {:type :string}
                               :schema.property/url {:type :string}}
                              :required [:name :schema.property/url]}
                             :date
                             {:type :string :format :date}
                             {:type :string})]
                       [k
                        (if (= :db.cardinality/many (:db/cardinality v))
                          {:type :array :items chat-property}
                          chat-property)])))
              (into {}))))

(defn- ->post-body [graph-properties args]
  (let [prompt (str "Tell me about movie " (pr-str (first args)))
        chat-properties (->chat-properties graph-properties)]
    {:model "llama3.2"
     :messages [{:role "user" :content prompt}]
     :stream false
     :format {:type :object
              :properties chat-properties
              :required (into [:name] (keys graph-properties))}}))

(defn- print-export-map
  [body graph-properties]
  (let [content-json (.. body -message -content)
        content (-> (js/JSON.parse content-json)
                    (js->clj :keywordize-keys true))
        obj-properties
        (->> (dissoc content :name)
             (map (fn [[k v]]
                    [k
                     (let [prop-value
                           (fn [e]
                             (case (get-in graph-properties [k :logseq.property/type])
                               :node
                               [:build/page (cond-> {:block/title (:name e)
                                                     :build/tags [:schema.class/Person]}
                                              (seq (dissoc e :name))
                                              (assoc :build/properties (dissoc e :name)))]
                               :date
                               [:build/page {:build/journal (parse-long (string/replace v "-" ""))}]
                               (str e)))]
                       (if (vector? v)
                         (set (map prop-value v))
                         (prop-value v)))]))
             (into {}))
        graph-classes {:schema.class/Person {}
                       :schema.class/Movie {}}
        export-map
        {:properties graph-properties
         :classes graph-classes
         :pages-and-blocks [{:page {:build/journal 20250221}
                             :blocks [{:block/title (:name content)
                                       :build/tags [:schema.class/Movie]
                                       :build/properties obj-properties}]}]}]
    (pprint/pprint export-map)))

(defn- structured-chat
  [graph-properties args]
  (let [post-body (->post-body graph-properties args)
        post-body' (clj->js post-body :keyword-fn #(subs (str %) 1))]
    (-> (p/let [resp (js/fetch "http://localhost:11434/api/chat"
                               #js {:method "POST"
                                    :headers #js {"Accept" "application/json"}
                                    :body (js/JSON.stringify post-body')})
                body (.json resp)]
          (if (= 200 (.-status resp))
            (print-export-map body graph-properties)
            (do
              (println "Error: Chat endpoint returned" (.-status resp) "with message" (pr-str (.-error body)))
              (js/process.exit 1))))
        (p/catch (fn [e]
                   (println "Unexpected error: " e))))))

(def spec
  "Options spec"
  {:help {:alias :h
          :desc "Print help"}
   :verbose {:alias :v
             :desc "Print more info"}})

(defn -main [& args]
  (let [[graph-dir & args'] args
        options (cli/parse-opts args' {:spec spec})
        _ (when (or (nil? graph-dir) (:help options))
            (println (str "Usage: $0 GRAPH-NAME [& ARGS] [OPTIONS]\nOptions:\n"
                          (cli/format-opts {:spec spec})))
            (js/process.exit 1))
        [dir db-name] (get-dir-and-db-name graph-dir)
        conn (sqlite-cli/open-db! dir db-name)
        graph-properties (->> (:schema.class/Movie user-config)
                              (map #(d/entity @conn %))
                              (map #(vector (:db/ident %) (select-keys % [:logseq.property/type :db/cardinality])))
                              (into {}))]
    (when (:verbose options) (println "DB contains" (count (d/datoms @conn :eavt)) "datoms"))
    (structured-chat graph-properties args')))

#js {:main -main}