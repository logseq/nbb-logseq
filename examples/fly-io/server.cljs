(ns server
  (:require
   ["express$default" :as express]
   [clojure.pprint :as pprint]
   [clojure.string :as string]
   [clojure.edn :as edn]
   [logseq.graph-parser.cli :as gp-cli]
   [logseq.graph-parser :as graph-parser]
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
                     _ (println "Query:" query)
                     res (map first
                              (d/q query @@db-conn))]
                 (with-out-str
                   (pprint/pprint res))))))

(.listen app port
         (fn []
           (println "Listening on port" port)
           ;; TODO: Move non-git files fetch into cli ns
           (let [files (->> (gp-cli/sh ["find" graph-dir "-type" "f"] {:stdio nil})
                            .-stdout
                            string/split-lines
                            (mapv #(hash-map :file/path % :file/content (gp-cli/slurp %)))
                            graph-parser/filter-files)
                 {:keys [conn files]}
                 (gp-cli/parse-graph graph-dir {:files files})]
             (println "Finished loading graph with" (count files) "files")
             (reset! db-conn conn))))
