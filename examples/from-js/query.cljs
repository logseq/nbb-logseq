(ns query
  "Script that parses the given graph and then runs a query on it"
  (:require [clojure.pprint :as pprint]
            [clojure.edn :as edn]
            [nbb.core :as nbb]
            [datascript.core :as d]
            [logseq.graph-parser.cli :as gp-cli]))

(defn- query
  [args]
  (if-not (= 2 (count args))
    (println "Usage: $0 GRAPH-DIR QUERY")
    (let [[graph-dir query-string] args
          {:keys [conn]} (gp-cli/parse-graph graph-dir {:verbose false})
          db @conn
          query' (edn/read-string query-string)
          results (map first (d/q query' db))]
      results)))

(defn cljs-query
  [args]
  (pprint/pprint (query args)))

(defn js-query
  [args]
  (clj->js (query (js->clj args))))

;; Only invoke when calling from commandline with nbb-logseq
(when (and *command-line-args* (= nbb/*file* (:file (meta #'cljs-query))))
  (cljs-query *command-line-args*))

#js {:queryGraph js-query}
