(ns multi-db
  "Script that clones and queries any two graphs given their git urls. Takes an
  optional third arg which is the datascript query to run. The first time a new
  git repo is given, the script clones it and creates a transit cache db for it.
  Subsequent script invocations read from the transit cache"
  (:require [logseq.graph-parser.cli :as gp-cli]
            [nbb.core :as nbb]
            [datascript.core :as d]
            [datascript.transit :as dt]
            [clojure.pprint :as pprint]
            [clojure.edn :as edn]
            ["fs" :as fs]
            ["child_process" :as child-process]))

(defn- sh
  "Run shell cmd synchronously and print to inherited streams by default. Aims
    to be similar to babashka.tasks/shell
TODO: Fail fast when process exits 1"
  [cmd opts]
  (child-process/spawnSync (first cmd)
                           (clj->js (rest cmd))
                           (clj->js (merge {:stdio "inherit"} opts))))

(defn- clone-graphs
  [graph-urls]
  (map (fn [graph-url]
         (let [graph-dir (str "graphs/" (re-find #"[^/]+$" graph-url))]
           (if (fs/existsSync graph-dir)
             (println "Graph" graph-url "is already cloned")
             (sh ["git" "clone" graph-url graph-dir] {}))
           graph-dir))
       graph-urls))

(defn- parse-dir-or-read-from-cache
  "Given a graph's dir and transit cache file, return datascript db.
Just does an existence check for cache. A timestamp check would be better"
  [dir cache-file]
  (if (fs/existsSync cache-file)
    (dt/read-transit-str (fs/readFileSync cache-file))
    (let [conn (:conn (gp-cli/parse-graph dir {:verbose false}))]
      (println "Writing cache file" cache-file)
      (fs/writeFileSync cache-file (dt/write-transit-str @conn))
      @conn)))

(defn -main [args]
  (if (< (count args) 2)
    (throw (ex-info "Usage: $0 GRAPH1 GRAPH2 [QUERY]" {}))
    (let [graph-dirs (clone-graphs (take 2 args))
          dbs (map #(parse-dir-or-read-from-cache % (str % ".cache.json"))
                   graph-dirs)
          default-query '[:find (pull $1 ?b [:block/name])
                          :in $1 $2
                          :where
                          [$1 ?b :block/name ?n]
                          [$2 ?b2 :block/name ?n]]
          query (or (some-> (get (vec args) 2) edn/read-string) default-query)]
      (pprint/pprint (apply d/q query dbs)))))

(when (= nbb/*file* (:file (meta #'-main)))
  (-main *command-line-args*))
