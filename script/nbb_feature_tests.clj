(ns nbb-feature-tests
  "Test runner for feature tests"
  (:require [babashka.tasks :as tasks]
            [babashka.fs :as fs]
            [clojure.string :as str]))

(defn- git-clone
  [git-cmd & args]
  (apply tasks/shell (str "git clone -c advice.detachedHead=false " git-cmd) args))

(defn datascript-tests
  []
  (println "Running datascript tests...")
  (let [feature-dir "test/libraries/datascript"]
    (when-not (fs/exists? feature-dir)
      (git-clone "-b 1.3.12 https://github.com/tonsky/datascript" feature-dir)
      (fs/copy "test/datascript_test_core.cljs"
               (str feature-dir "/test/datascript/test/core.cljc")
               {:replace-existing true}))
    (tasks/shell "node lib/nbb_main.js -cp"
                 (str feature-dir "/test")
                 "test/datascript_test_runner.cljs")))

(defn datascript-transit-tests
  []
  (println "Running datascript-transit tests...")
  (let [feature-dir "test/libraries/datascript-transit"
        transit-test-file (str feature-dir "/test/datascript/test/transit.cljs")]
    (when-not (fs/exists? feature-dir)
      (git-clone "-b 0.3.0 https://github.com/tonsky/datascript-transit" feature-dir)
      ;; NBB_WORKAROUND: enable-console-print! and cljs.test/run-all-tests don't exist
      (spit transit-test-file
            (str/replace-first (slurp transit-test-file)
                               "(defn ^:export test_all"
                               "#_(defn ^:export test_all")))
    (tasks/shell "node lib/nbb_main.js -cp"
                 (str feature-dir "/test")
                 "test/datascript_transit_test_runner.cljs")))

(defn linked-tests
  []
  (println "Running linked tests...")
  (let [feature-dir "test/libraries/linked"
        map-test-file (str feature-dir "/test/linked/map_test.cljc")]
    (when-not (fs/exists? feature-dir)
      (git-clone "-b v1.3.0 https://github.com/frankiesardo/linked" feature-dir)
      ;; NBB_WORKAROUND: cljs.reader/read-string doesn't exist
      (spit map-test-file
            (str/replace (slurp map-test-file) "cljs.reader" "clojure.edn")))
    (tasks/shell "node lib/nbb_main.js -cp"
                 (str feature-dir "/test")
                 "test/linked_test_runner.cljs")))

(defn cljs-time-tests
  "Didn't run from source since such a small part of the library is available"
  []
  (println "Running cljs-time tests...")
  (tasks/shell "node lib/nbb_main.js -cp"
               "test"
               "test/cljs_time_test_runner.cljs"))

(defn main
  "Runs feature tests"
  []
  (datascript-tests)
  (datascript-transit-tests)
  (linked-tests)
  (cljs-time-tests))
