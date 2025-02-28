(ns logseq.ollama-chat
  (:require [babashka.cli :as cli]
            [cljs.pprint :as pprint]
            [clojure.string :as string]
            [datascript.core :as d]
            [logseq.common.util :as common-util]
            [logseq.common.util.date-time :as date-time-util]
            #_:clj-kondo/ignore
            [logseq.db.sqlite.cli :as sqlite-cli]
            [logseq.db.sqlite.export :as sqlite-export]
            [malli.json-schema :as json-schema]
            [promesa.core :as p]
            ["path" :as node-path]
            ["os" :as os]))

(def user-config
  "Configure what to query per class/tag. Each class has a map consisting of the following keys:
   * :chat/class-properties - a vec of properties to fetch for this class
   * :properties - Configures properties. Keys are the property kw idents and the values are a map with the keys:
     * :build/tags - For :node properties, tags to set for the property value(s)
     * :chat/properties - For :node properties, properties to fetch for the property value(s)"
  {:schema.class/Movie
   {:chat/class-properties [:schema.property/actor :schema.property/director :schema.property/musicBy
                            :schema.property/datePublished :schema.property/url]
    :properties
    {:schema.property/actor
     {:chat/properties [:schema.property/url #_ #_:schema.property/birthDate :schema.property/hasOccupation]}
     :schema.property/director
     {:chat/properties [:schema.property/url]}
     :schema.property/musicBy
     {:build/tags [:schema.class/MusicGroup] :chat/properties [:schema.property/url]}}}

   :schema.class/Book
   {:chat/class-properties [:schema.property/author :schema.property/datePublished :schema.property/url
                            #_ #_:schema.property/abridged :schema.property/numberOfPages]
    :properties
    {:schema.property/author
     {:build/tags [:schema.class/Person] :chat/properties [:schema.property/url]}}}

   :schema.class/Person
   {:chat/class-properties [:schema.property/birthDate :schema.property/birthPlace :schema.property/hasOccupation]
    ;; TODO: Get back a more specific place e.g. Country
    #_:properties
    #_{:schema.property/birthPlace
       {:chat-ident :birthCountry :build/tags [:schema.class/Country]
        :chat/properties [:schema.property/additionalType]}}}

   :schema.class/Organization
   {:chat/class-properties [:schema.property/url :schema.property/foundingLocation :schema.property/alumni]}

   :schema.class/MusicRecording
   {:chat/class-properties [:schema.property/byArtist :schema.property/inAlbum :schema.property/datePublished
                            :schema.property/url]
    :properties
    {:schema.property/byArtist
     {:build/tags [:schema.class/MusicGroup] :chat/properties [:schema.property/url]}
     :schema.property/inAlbum
     {:chat/properties [:schema.property/url]}}}})

(defn- get-dir-and-db-name
  "Gets dir and db name for use with open-db!"
  [graph-dir]
  (if (string/includes? graph-dir "/")
    (let [graph-dir'
          (node-path/join (or js/process.env.ORIGINAL_PWD ".") graph-dir)]
      ((juxt node-path/dirname node-path/basename) graph-dir'))
    [(node-path/join (os/homedir) "logseq" "graphs") graph-dir]))

(defn- define-chat-schema
  "Returns a vec of optional malli properties and required schema for a given property ident"
  [input-class export-properties prop-ident]
  (let [prop->malli-type {:number :int
                          :checkbox :boolean
                          ;; doesn't matter since we're overridding it
                          :date :any
                          ;; TODO: Add support for :datetime
                          :url :string
                          :default :string}
        prop-type (get-in export-properties [prop-ident :logseq.property/type])
        schema* (if (= :node prop-type)
                  (let [obj-properties (get-in user-config [input-class :properties prop-ident :chat/properties])]
                    (into
                     [:map [:name :string]]
                     (map #(apply vector % (define-chat-schema input-class export-properties %))
                          obj-properties)))
                  (get prop->malli-type prop-type))
        _ (assert schema* (str "Property type " (pr-str prop-type) " must have a schema type"))
        schema (if (= :db.cardinality/many (get-in export-properties [prop-ident :db/cardinality]))
                 [:sequential schema*]
                 schema*)
        props-and-schema (cond-> []
                           ;; Add json-schemable :date via malli property rather fun install fun of malli.experimental.time
                           (= :date prop-type)
                           (conj {:json-schema {:type "string" :format "date"}})
                           true
                           (conj schema))]
    props-and-schema))

(defn- ->chat-properties-schema [input-class export-properties]
  (into
   [:map [:name :string]]
   (map
    (fn [k]
      (apply vector
             (or (get-in user-config [input-class :properties k :chat-ident]) k)
             (define-chat-schema input-class export-properties k)))
    (:chat/class-properties (input-class user-config)))))

(defn- generate-json-schema-format [input-class export-properties]
  ;; (pprint/pprint (->chat-properties-schema input-class export-properties))
  (json-schema/transform (->chat-properties-schema input-class export-properties)))

(defn- ->post-body [input-class export-properties args]
  (let [prompt (str "Tell me about " (first args) " " (pr-str (string/join " " (rest args))))]
    {:model "llama3.2"
     :messages [{:role "user" :content prompt}]
     :stream false
     :format (generate-json-schema-format input-class export-properties)}))

(defn- buildable-properties [properties input-class export-properties]
  (->> properties
       (map (fn [[chat-ident v]]
              (let [prop-ident (or (some (fn [[k' v']] (when (= chat-ident (:chat-ident v')) k'))
                                         (:properties (get user-config input-class)))
                                   chat-ident)]
                [prop-ident
                 (let [prop-value
                       (fn [e]
                         (case (get-in export-properties [prop-ident :logseq.property/type])
                           :node
                           [:build/page (let [obj-tags (or (get-in user-config [input-class :properties prop-ident :build/tags])
                                                           (some-> (get-in export-properties [prop-ident :build/property-classes])
                                                                   (subvec 0 1)))]
                                          (cond-> {:block/title (:name e)}
                                            (seq obj-tags)
                                            (assoc :build/tags obj-tags)
                                            (seq (dissoc e :name))
                                            (assoc :build/properties
                                                   (buildable-properties (dissoc e :name) input-class export-properties))))]
                           :date
                           [:build/page {:build/journal (parse-long (string/replace e "-" ""))}]
                           (:number :checkbox)
                           e
                           (:default :url)
                           (str e)))]
                   (if (vector? v)
                     (set (map prop-value v))
                     (prop-value v)))])))
       (into {:user.property/importedAt (common-util/time-ms)})))

(defn- print-export-map
  [body input-class export-properties {:keys [block-import]}]
  (let [content-json (.. body -message -content)
        content (-> (js/JSON.parse content-json)
                    (js->clj :keywordize-keys true))
        obj-properties (buildable-properties (dissoc content :name) input-class export-properties)
        export-classes (merge (zipmap (distinct
                                       (concat (mapcat :build/tags (vals (:properties (get user-config input-class))))
                                               ;; We may not use all of these but easier than walking build/page's
                                               (mapcat :build/property-classes (vals export-properties))))
                                      (repeat {}))
                              {input-class {}})
        pages-and-blocks
        (if block-import
          [{:page {:build/journal (date-time-util/date->int (new js/Date))}
            :blocks [{:block/title (:name content)
                      :build/tags [input-class]
                      :build/properties obj-properties}]}]
          [{:page {:block/title (:name content)
                   :build/tags [input-class]
                   ;; Allows upsert of existing page
                   :build/keep-uuid? true
                   :build/properties obj-properties}}])
        export-map
        {:properties (merge export-properties
                            {:user.property/importedAt
                             {:logseq.property/type :datetime
                              :db/cardinality :db.cardinality/one}})
         :classes export-classes
         :pages-and-blocks pages-and-blocks}]
    (#'sqlite-export/ensure-export-is-valid export-map)
    (pprint/pprint export-map)))

(defn- structured-chat
  [input-class export-properties args options]
  (let [post-body (->post-body input-class export-properties args)
        post-body' (clj->js post-body :keyword-fn #(subs (str %) 1))]
    ;; TODO: Try javascript approach for possibly better results
    ;; Uses chat endpoint as described in https://ollama.com/blog/structured-outputs
    (-> (p/let [resp (js/fetch "http://localhost:11434/api/chat"
                               #js {:method "POST"
                                    :headers #js {"Accept" "application/json"}
                                    :body (js/JSON.stringify post-body')})
                body (.json resp)]
          (if (= 200 (.-status resp))
            (if (:raw options)
              (pprint/pprint (update-in (js->clj body :keywordize-keys true)
                                        [:message :content]
                                        #(-> (js/JSON.parse %)
                                             (js->clj :keywordize-keys true))))
              (print-export-map body input-class export-properties options))
            (do
              (println "Error: Chat endpoint returned" (.-status resp) "with message" (pr-str (.-error body)))
              (js/process.exit 1))))
        (p/catch (fn [e]
                   (println "Unexpected error: " e))))))

(def spec
  "Options spec"
  {:help {:alias :h
          :desc "Print help"}
   :block-import {:alias :b
                  :desc "Import object as block in today's journal"}
   :raw {:alias :r
         :desc "Print raw json chat response instead of Logseq EDN"}
   :json-schema-inspect {:alias :j
                         :desc "Print json schema to submit and don't submit to chat"}
   :verbose {:alias :v
             :desc "Print more info"}})

(defn- error [msg]
  (println (str "Error: " msg))
  (js/process.exit 1))

(defn -main [& args]
  (let [[graph-dir & args'] args
        options (cli/parse-opts args' {:spec spec})
        _ (when (or (nil? graph-dir) (:help options))
            (println (str "Usage: $0 GRAPH-NAME TAG [& ARGS] [OPTIONS]\nOptions:\n"
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
        export-properties (->> (:chat/class-properties (input-class user-config))
                               (concat (mapcat :chat/properties (vals (:properties (get user-config input-class)))))
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
    (if (:json-schema-inspect options)
      (pprint/pprint (generate-json-schema-format input-class export-properties))
      (structured-chat input-class export-properties args' options))))

#js {:main -main}