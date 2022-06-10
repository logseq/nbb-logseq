(ns server
  "Starts a web server that can process queries for graph at resources/graph"
  (:require
   ["express$default" :as express]
   [clojure.pprint :as pprint]
   [clojure.edn :as edn]
   [logseq.graph-parser.cli :as gp-cli]
   [logseq.db.rules :as rules]
   [datascript.core :as d]))

(def app (express))
(def port (or js/process.env.PORT 8092))
(def db-conn (atom nil))
(def graph-dir "resources/graph")

(.get app "/"
      (fn foo [_req res]
        (.send res
               "Hello world!")))

(.get app "/q"
      (fn foo [req res]
        (.set res "content-type" "text/plain")
        (.send res
               (let [query (or (some-> req .-query .-q js/decodeURI edn/read-string)
                               ;; default to a query likely to provide results
                               '[:find (pull ?b [:block/name])
                                 :where [?b :block/name]])
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
