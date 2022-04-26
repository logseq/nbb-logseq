(ns cljs-time-test-runner
  (:require [cljs.test :as t]
            [nbb.core :as nbb]
            [cljs-time-format-test]))

(defn init []
  (t/run-tests 'cljs-time-format-test))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
