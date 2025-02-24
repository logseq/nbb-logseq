(ns logseq.ollama-chat
  (:require [babashka.cli :as cli]
            [cljs.pprint :as pprint]
            [clojure.string :as string]
            [datascript.core :as d]
            [logseq.common.util :as common-util]
            [logseq.common.util.date-time :as date-time-util]
            #_:clj-kondo/ignore
            [logseq.db.sqlite.cli :as sqlite-cli]
            [promesa.core :as p]
            ["path" :as node-path]
            ["os" :as os]))

(def user-config
  {:schema.class/Movie
   {:schema.property/actor
    {:chat-properties [:schema.property/url]}
    :schema.property/director
    {:chat-properties [:schema.property/url]}
    :schema.property/musicBy
    {:build/tags [:schema.class/MusicGroup] :chat-properties [:schema.property/url]}
    :schema.property/datePublished {}
    :schema.property/url {}}
   :schema.class/Book
   {:schema.property/author
    {:build/tags [:schema.class/Person] :chat-properties [:schema.property/url :schema.property/birthDate]}
    :schema.property/datePublished {}}
   :schema.class/MusicRecording
   {:schema.property/byArtist
    {:build/tags [:schema.class/MusicGroup] :chat-properties [:schema.property/url]}
    :schema.property/inAlbum
    {:chat-properties [:schema.property/url]}
    :schema.property/datePublished {}}})

(defn- get-dir-and-db-name
  "Gets dir and db name for use with open-db!"
  [graph-dir]
  (if (string/includes? graph-dir "/")
    (let [graph-dir'
          (node-path/join (or js/process.env.ORIGINAL_PWD ".") graph-dir)]
      ((juxt node-path/dirname node-path/basename) graph-dir'))
    [(node-path/join (os/homedir) "logseq" "graphs") graph-dir]))

(defn define-chat-property [input-class export-properties prop-ident]
  (case (get-in export-properties [prop-ident :logseq.property/type])
    :node
    (let [obj-properties (get-in user-config [input-class prop-ident :chat-properties])]
      {:type :object
       :properties
       (merge {:name {:type :string}}
              (->> obj-properties
                   (map #(vector % (define-chat-property input-class export-properties %)))
                   (into {})))
       :required (vec (concat [:name] obj-properties))})
    :date
    {:type :string :format :date}
    {:type :string}))

(defn- ->chat-properties [input-class export-properties]
  (merge {:name {:type :string}}
         (->> export-properties
              (map (fn [[k v]]
                     (let [chat-property (define-chat-property input-class export-properties k)]
                       [k
                        (if (= :db.cardinality/many (:db/cardinality v))
                          {:type :array :items chat-property}
                          chat-property)])))
              (into {}))))

(defn- ->post-body [input-class export-properties args]
  (let [prompt (str "Tell me about " (first args) " " (pr-str (string/join " " (rest args))))
        chat-properties (->chat-properties input-class export-properties)]
    {:model "llama3.2"
     :messages [{:role "user" :content prompt}]
     :stream false
     :format {:type :object
              :properties chat-properties
              :required (into [:name] (keys export-properties))}}))

(defn- buildable-properties [properties input-class export-properties]
  (->> properties
       (map (fn [[k v]]
              [k
               (let [prop-value
                     (fn [e]
                       (case (get-in export-properties [k :logseq.property/type])
                         :node
                         [:build/page (let [obj-tags (or (get-in user-config [input-class k :build/tags])
                                                         (some-> (get-in export-properties [k :build/property-classes])
                                                                 (subvec 0 1)))]
                                        (cond-> {:block/title (:name e)}
                                             (seq obj-tags)
                                             (assoc :build/tags obj-tags)
                                             (seq (dissoc e :name))
                                             (assoc :build/properties
                                                    (buildable-properties (dissoc e :name) input-class export-properties))))]
                         :date
                         [:build/page {:build/journal (parse-long (string/replace e "-" ""))}]
                         (str e)))]
                 (if (vector? v)
                   (set (map prop-value v))
                   (prop-value v)))]))
       (into {:user.property/importedAt (common-util/time-ms)})))

(defn- print-export-map
  [body input-class export-properties]
  (let [content-json (.. body -message -content)
        content (-> (js/JSON.parse content-json)
                    (js->clj :keywordize-keys true))
        obj-properties (buildable-properties (dissoc content :name) input-class export-properties)
        export-classes (merge (zipmap (distinct
                                       (concat (mapcat :build/tags (vals (get user-config input-class)))
                                               ;; We may not use all of these but easier than walking build/page's
                                               (mapcat :build/property-classes (vals export-properties))))
                                      (repeat {}))
                              {input-class {}})
        today-int (date-time-util/date->int (new js/Date))
        export-map
        {:properties (merge export-properties
                            {:user.property/importedAt
                             {:logseq.property/type :datetime
                              :db/cardinality :db.cardinality/one}})
         :classes export-classes
         :pages-and-blocks [{:page {:build/journal today-int}
                             :blocks [{:block/title (:name content)
                                       :build/tags [input-class]
                                       :build/properties obj-properties}]}]}]
    (pprint/pprint export-map)))

(defn- structured-chat
  [input-class export-properties args]
  (let [post-body (->post-body input-class export-properties args)
        post-body' (clj->js post-body :keyword-fn #(subs (str %) 1))]
    (-> (p/let [resp (js/fetch "http://localhost:11434/api/chat"
                               #js {:method "POST"
                                    :headers #js {"Accept" "application/json"}
                                    :body (js/JSON.stringify post-body')})
                body (.json resp)]
          (if (= 200 (.-status resp))
            (print-export-map body input-class export-properties)
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

(defn- error [msg]
  (println (str "Error: " msg))
  (js/process.exit 1))

(defn -main [& args]
  (let [[graph-dir & args'] args
        options (cli/parse-opts args' {:spec spec})
        _ (when (or (nil? graph-dir) (:help options))
            (println (str "Usage: $0 GRAPH-NAME [& ARGS] [OPTIONS]\nOptions:\n"
                          (cli/format-opts {:spec spec})))
            (js/process.exit 1))
        [dir db-name] (get-dir-and-db-name graph-dir)
        conn (sqlite-cli/open-db! dir db-name)
        input-class
        (or (->>
             (d/q '[:find [(pull ?b [:db/ident]) ...]
                    :in $ ?name
                    :where [?b :block/tags :logseq.class/Tag] [?b :block/name ?name]]
                  @conn
                  (string/lower-case (first args')))
             first
             :db/ident)
            (error (str "No class found for" (pr-str (first args')))))
        export-properties (->> (keys (input-class user-config))
                               (concat (mapcat :chat-properties (vals (get user-config input-class))))
                               distinct
                               (map #(or (d/entity @conn %)
                                         (error (str "No property exists for " (pr-str %)))))
                               (map #(vector (:db/ident %)
                                             (cond-> (select-keys % [:logseq.property/type :db/cardinality])
                                               (seq (:logseq.property/classes %))
                                               (assoc :build/property-classes
                                                      (mapv :db/ident (:logseq.property/classes %))))))
                               (into {}))]
    (when (:verbose options) (println "DB contains" (count (d/datoms @conn :eavt)) "datoms"))
    (structured-chat input-class export-properties args')))

#js {:main -main}