(ns datascript-transit-test-runner
  (:require [cljs.test :as t]
            [nbb.core :as nbb]
            [datascript.test.transit]))

(defn init []
  (t/run-tests 'datascript.test.transit))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
