(ns nbb.impl.linked
  {:no-doc true}
  (:require [linked.core :as linked]
            [nbb.core :as nbb]
            [sci.core :as sci :refer [copy-var]]))

(def core-ns (sci/create-ns 'linked.core nil))

(def core-namespace
  {'map (copy-var linked/map core-ns)})

(defn init []
  (nbb/register-plugin!
   ::linked
   {:namespaces {'linked.core core-namespace}}))
