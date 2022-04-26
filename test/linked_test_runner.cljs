(ns linked-test-runner
  (:require [cljs.test :as t]
            [nbb.core :as nbb]
            [linked.map-test]))

(defn init []
  (t/run-tests 'linked.map-test))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
