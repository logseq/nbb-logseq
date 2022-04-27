(ns nbb-feature-tests
  "Test runner for feature tests"
  (:require [babashka.tasks :as tasks]))

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
  (cljs-time-tests))
