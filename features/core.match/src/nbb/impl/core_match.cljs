(ns nbb.impl.core-match
  {:no-doc true}
  (:require [cljs.core.match :as match]
            [nbb.core :as nbb]
            [sci.core :as sci]))

(def core-ns (sci/create-ns 'cljs.core.match nil))
(def core-namespace
  {'match (sci/copy-var match/match core-ns)})

(def config {:namespaces {'cljs.core.match core-namespace}})

(defn init []
  (nbb/register-plugin! ::core-match config))
