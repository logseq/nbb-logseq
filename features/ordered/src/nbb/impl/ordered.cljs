(ns nbb.impl.ordered
  {:no-doc true}
  (:require [flatland.ordered.map]
            [nbb.core :as nbb]
            [sci.core :as sci]))

(def map-ns (sci/create-ns 'flatland.ordered.map nil))
(def map-namespace (sci/copy-ns flatland.ordered.map map-ns))

(def config {:namespaces {'flatland.ordered.map map-namespace}})

(defn init []
  (nbb/register-plugin! ::ordered config))
