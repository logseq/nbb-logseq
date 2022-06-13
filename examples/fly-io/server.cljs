(ns server
  "Starts a web server that can process queries for graph at resources/graph"
  (:require
   ["express$default" :as express]
   ["fs" :as fs]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [goog.string :as gstring]
   [logseq.graph-parser.cli :as gp-cli]
   [logseq.db.rules :as rules]
   [datascript.core :as d]))

(def app (express))
(def port (or js/process.env.PORT 8092))
(def db-conn (atom nil))
(def graph-dir "resources/graph")

(def index-body
  (gstring/format
   (str (fs/readFileSync "resources/public/index.html"))
   (if-let [url (and (fs/existsSync (str graph-dir "/.git/config"))
                     (->> (str (fs/readFileSync (str graph-dir "/.git/config")))
                          (re-find #"url\s*=\s*(\S+)")
                          second))]
     (gstring/format "the <a href=\"%s\">%s</a>" url url)
     "your")))

(.get app "/"
      (fn foo [_req res]
        (.send res index-body)))

(.use app
      (.static express "resources/public"))

(.get app "/q"
      (fn foo [req res]
        (.set res "content-type" "text/plain")
        (.send res
               (let [query (some-> req .-query .-q js/decodeURI edn/read-string)
                     add-rules? (not (contains? (set query) :in))
                     query' (if add-rules? (into query [:in '$ '%]) query)
                     _ (println "Query:" (pr-str query'))
                     res (map first
                              (apply d/q query' @@db-conn
                                (if add-rules? [(vals rules/query-dsl-rules)] [])))]
                 (with-out-str
                   (pprint/pprint res))))))

(.listen app port
         (fn []
           (println "Listening on port" port)
           (let [{:keys [conn files]} (gp-cli/parse-graph graph-dir)]
             (println "Finished loading graph with" (count files) "files")
             (reset! db-conn conn))))
