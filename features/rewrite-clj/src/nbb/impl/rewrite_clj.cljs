(ns ^:no-doc nbb.impl.rewrite-clj
  (:require [rewrite-clj.node]
            [rewrite-clj.paredit]
            [rewrite-clj.parser]
            [rewrite-clj.zip]
            [sci.core :as sci]
            [nbb.core :as nbb]))

(def nns (sci/create-ns 'rewrite-clj.node nil))
(def pns (sci/create-ns 'rewrite-clj.parser nil))
(def pens (sci/create-ns 'rewrite-clj.paredit nil))
(def zns (sci/create-ns 'rewrite-clj.zip nil))

(def node-namespace
  (sci/copy-ns rewrite-clj.node nns))

(def parser-namespace
  (sci/copy-ns rewrite-clj.parser pns))

(def paredit-namespace
  (sci/copy-ns rewrite-clj.paredit pens))

(def zip-namespace
  (sci/copy-ns rewrite-clj.zip zns))

(def namespaces {'rewrite-clj.node node-namespace
                 'rewrite-clj.parser parser-namespace
                 'rewrite-clj.paredit paredit-namespace
                 'rewrite-clj.zip zip-namespace})

(def config {:namespaces namespaces})

(defn init []
  (nbb/register-plugin!
   ::rewrite-clj
   config))