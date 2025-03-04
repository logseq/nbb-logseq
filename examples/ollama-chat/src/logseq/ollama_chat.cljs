(ns logseq.ollama-chat
  (:require [babashka.cli :as cli]
            [cljs.pprint :as pprint]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [datascript.core :as d]
            [logseq.common.util :as common-util]
            [logseq.common.util.date-time :as date-time-util]
            #_:clj-kondo/ignore
            [logseq.db.sqlite.cli :as sqlite-cli]
            [logseq.db.sqlite.export :as sqlite-export]
            [malli.json-schema :as json-schema]
            [promesa.core :as p]
            ["child_process" :as child-process]
            ["path" :as node-path]
            ["os" :as os]
            [logseq.db :as ldb]))

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
     {:chat/properties [:schema.property/birthDate #_:schema.property/hasOccupation]}
     :schema.property/musicBy
     {:build/tags [:schema.class/MusicGroup]}}}

   :schema.class/Book
   {:chat/class-properties [:schema.property/author :schema.property/datePublished :schema.property/url
                            #_#_:schema.property/abridged :schema.property/numberOfPages]
    :properties
    {:schema.property/author
     {:build/tags [:schema.class/Person]}}}

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
     {:build/tags [:schema.class/MusicGroup]}}}})

(defn copy-to-clipboard [text]
  (let [proc (child-process/exec "pbcopy")]
    (.write (.-stdin proc) text)
    (.end (.-stdin proc))))

(defn- command-exists? [command]
  (try
    (child-process/execSync (str "which " command) #js {:stdio "ignore"})
    true
    (catch :default _ false)))

(defn- get-dir-and-db-name
  "Gets dir and db name for use with open-db!"
  [graph-dir]
  (if (string/includes? graph-dir "/")
    (let [graph-dir'
          (node-path/join (or js/process.env.ORIGINAL_PWD ".") graph-dir)]
      ((juxt node-path/dirname node-path/basename) graph-dir'))
    [(node-path/join (os/homedir) "logseq" "graphs") graph-dir]))

(defn- ->property-value-schema
  "Returns a vec of optional malli properties and required schema for a given property ident"
  [{:keys [input-class input-global-properties] :as input-map} export-properties prop-ident]
  (let [prop->malli-type {:number :int
                          :checkbox :boolean
                          ;; doesn't matter since we're overridding it
                          :date :any
                          ;; TODO: Add support for :datetime
                          :url uri?
                          :default :string}
        prop-type (get-in export-properties [prop-ident :logseq.property/type])
        schema* (if (= :node prop-type)
                  (let [obj-properties (->> (get-in user-config [input-class :properties prop-ident :chat/properties])
                                            (concat input-global-properties)
                                            distinct)]
                    (into
                     [:map [:name {:min 2} :string]]
                     (map #(apply vector % (->property-value-schema input-map export-properties %))
                          obj-properties)))
                  (get prop->malli-type prop-type))
        _ (assert schema* (str "Property type " (pr-str prop-type) " must have a schema type"))
        schema (if (= :db.cardinality/many (get-in export-properties [prop-ident :db/cardinality]))
                 [:sequential {:min 1} schema*]
                 schema*)
        props-and-schema (cond-> []
                           ;; Add json-schemable :date via malli property rather fun install fun of malli.experimental.time
                           (= :date prop-type)
                           (conj {:json-schema {:type "string" :format "date"}})
                           true
                           (conj schema))]
    props-and-schema))

(defn- ->query-schema [{:keys [input-class input-properties input-global-properties] :as input-map}
                       export-properties {:keys [many-objects]}]
  (let [schema
        (into
         [:map [:name {:min 2} :string]]
         (map
          (fn [k]
            (apply vector
                   (or (get-in user-config [input-class :properties k :chat-ident]) k)
                   (->property-value-schema input-map export-properties k)))
          (distinct
           (concat (:chat/class-properties (input-class user-config))
                   input-properties
                   input-global-properties))))]
    (if many-objects
      [:sequential {:min 1} schema]
      schema)))

(defn- generate-json-schema-format [input-map export-properties options]
  ;; (pprint/pprint (->query-schema input-map export-properties options))
  (json-schema/transform (->query-schema input-map export-properties options)))

(defn- ->post-body [input-map export-properties args options]
  (let [prompt (if (:many-objects options)
                 (str "Tell me about " (first args) "(s) "
                      (->> (string/split (string/join " " (rest args)) #"\s*,\s*")
                           (map #(pr-str %))
                           (string/join ", ")))
                 (str "Tell me about " (first args) " " (pr-str (string/join " " (rest args)))))]
    {:model "llama3.2"
     :messages [{:role "user" :content prompt}]
     :stream false
     :format (generate-json-schema-format input-map export-properties options)}))

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

(defn- remove-invalid-properties [pages-and-blocks*]
  (let [urls (atom #{})
        _ (walk/postwalk (fn [f]
                           (when (and (vector? f) (= :schema.property/url (first f)))
                             (swap! urls conj (second f)))
                           f)
                         pages-and-blocks*)
        ;; Won't need regex check if ollama ever supports regex 'pattern' option
        invalid-urls (remove #(re-find #"^(https?)://.*$" %) @urls)
        pages-and-blocks (walk/postwalk (fn [f]
                                          (if (and (map? f) (contains? (set invalid-urls) (:schema.property/url f)))
                                            (dissoc f :schema.property/url)
                                            f))
                                        pages-and-blocks*)]
    {:pages-and-blocks pages-and-blocks
     :urls @urls
     :invalid-urls invalid-urls}))

(defn- print-export-map
  [body input-class export-properties {:keys [block-import many-objects]}]
  (let [content-json (.. body -message -content)
        content (-> (js/JSON.parse content-json)
                    (js->clj :keywordize-keys true))
        objects (mapv #(hash-map :name (:name %)
                                 :properties (buildable-properties (dissoc % :name) input-class export-properties))
                      (if many-objects content [content]))
        export-classes (merge (zipmap (distinct
                                       (concat (mapcat :build/tags (vals (:properties (get user-config input-class))))
                                               ;; We may not use all of these but easier than walking build/page's
                                               (mapcat :build/property-classes (vals export-properties))))
                                      (repeat {}))
                              {input-class {}})
        pages-and-blocks*
        (if block-import
          [{:page {:build/journal (date-time-util/date->int (new js/Date))}
            :blocks (mapv (fn [obj]
                            {:block/title (:name obj)
                             :build/tags [input-class]
                             :build/properties (:properties obj)})
                          objects)}]
          (mapv (fn [obj]
                  {:page
                   {:block/title (:name obj)
                    :build/tags [input-class]
                   ;; Allows upsert of existing page
                    :build/keep-uuid? true
                    :build/properties (:properties obj)}})
                objects))
        {:keys [pages-and-blocks invalid-urls urls]} (remove-invalid-properties pages-and-blocks*)
        export-map
        {:properties (merge export-properties
                            {:user.property/importedAt
                             {:logseq.property/type :datetime
                              :db/cardinality :db.cardinality/one}})
         :classes export-classes
         :pages-and-blocks pages-and-blocks}]
    (#'sqlite-export/ensure-export-is-valid export-map)
    (pprint/pprint export-map)
    (when (command-exists? "pbcopy")
      (copy-to-clipboard (with-out-str (pprint/pprint export-map))))
    (when (seq invalid-urls)
      (println (str (count invalid-urls) "/" (count urls)) "urls were removed for being invalid:" (pr-str (vec invalid-urls))))))

(defn- structured-chat
  [{:keys [input-class] :as input-map} export-properties args options]
  (let [post-body (->post-body input-map export-properties args options)
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
   :raw {:alias :R
         :desc "Print raw json chat response instead of Logseq EDN"}
   :json-schema-inspect {:alias :j
                         :desc "Print json schema to submit and don't submit to chat"}
   :properties {:alias :p
                :desc "Properties to fetch for top-level object"
                :coerce []}
   :global-properties {:alias :P
                       :desc "Global properties to fetch for all objects"
                       :coerce []
                       :default ["url"]}
   :random-properties {:alias :r
                       :desc "Random number of properties to fetch for top-level object"
                       :coerce :long}
   :many-objects {:alias :m
                  :desc "Query is for multiple comma separated objects"}})

(defn- error [msg]
  (println (str "Error: " msg))
  (js/process.exit 1))

(defn- translate-input-property [input]
  (if (= "description" input) :logseq.property/description (keyword "schema.property" input)))

(defn- get-class-properties
  [class]
  (let [class-parents (ldb/get-classes-parents [class])]
    (->> (mapcat (fn [class]
                   (:logseq.property.class/properties class))
                 (concat [class] class-parents))
         (map :db/ident)
         distinct)))

(defn -main [& args]
  (let [{options :opts args' :args} (cli/parse-args args {:spec spec})
        [graph-dir & args''] args'
        _ (when (or (nil? graph-dir) (:help options))
            (println (str "Usage: $0 GRAPH-NAME TAG [& ARGS] [OPTIONS]\nOptions:\n"
                          (cli/format-opts {:spec spec})))
            (js/process.exit 1))
        [dir db-name] (get-dir-and-db-name graph-dir)
        conn (sqlite-cli/open-db! dir db-name)
        input-class-ent
        (or (->>
             (d/q '[:find [?b ...]
                    :in $ ?name
                    :where [?b :block/tags :logseq.class/Tag] [?b :block/name ?name]]
                  @conn
                  (string/lower-case (first args'')))
             first
             (d/entity @conn))
            (error (str "No class found for" (pr-str (first args'')))))
        input-class (:db/ident input-class-ent)
        random-properties (when (:random-properties options)
                            (take (:random-properties options)
                                  (shuffle (get-class-properties input-class-ent))))
        _ (when (seq random-properties)
            (println "To recreate these random properties: -p"
                     (string/join " " (map name random-properties))))
        input-map {:input-class input-class
                   :input-properties (into (mapv translate-input-property (:properties options))
                                           random-properties)
                   :input-global-properties
                   (mapv translate-input-property
                         ;; Use -P to clear default
                         (if (= (:global-properties options) [true]) [] (:global-properties options)))}
        export-properties (->> (:chat/class-properties (input-class user-config))
                               (concat (mapcat :chat/properties (vals (:properties (get user-config input-class)))))
                               (concat (:input-properties input-map))
                               (concat (:input-global-properties input-map))
                               distinct
                               (map #(or (d/entity @conn %)
                                         (error (str "No property exists for " (pr-str %)))))
                               (map #(vector (:db/ident %)
                                             (cond-> (select-keys % [:logseq.property/type :db/cardinality])
                                               (seq (:logseq.property/classes %))
                                               (assoc :build/property-classes
                                                      (mapv :db/ident (:logseq.property/classes %))))))
                               (into {}))]
    (if (:json-schema-inspect options)
      (pprint/pprint (generate-json-schema-format input-map export-properties options))
      (structured-chat input-map export-properties args'' options))))

#js {:main -main}