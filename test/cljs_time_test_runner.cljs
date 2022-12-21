(ns cljs-time-test-runner
  (:require [cljs.test :as t]
            [nbb.core :as nbb]
            [cljs-time-format-test]))

(defmethod t/report [::t/default :end-run-tests] [m]
  (let [{:keys [error fail]} m]
    (if (pos? (+ error fail))
      (js/process.exit 1)
      (js/process.exit 0))))

(defn init []
  (t/run-tests 'cljs-time-format-test))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
