(ns nbb.impl.cljs-bean-transit
  {:no-doc true}
  (:require [cljs-bean.transit]
            [nbb.core :as nbb]
            [sci.core :as sci]))

(def btns (sci/create-ns 'cljs-bean.transit nil))
(def cljs-bean-transit-namespace
  (sci/copy-ns cljs-bean.transit btns))

(defn init []
  (nbb/register-plugin!
   ::cljs-bean-transit
   {:namespaces {'cljs-bean.transit cljs-bean-transit-namespace}}))