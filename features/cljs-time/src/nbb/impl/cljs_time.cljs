(ns nbb.impl.cljs-time
  "Only some fns are required by logseq.graph-parser and are documented.
Remaining fns are used by the community"
  (:require [cljs-time.core :as t]
            [cljs-time.coerce :as tc]
            [cljs-time.format :as tf]
            [nbb.core :as nbb]
            [sci.core :as sci :refer [copy-var]]))

(def core-ns (sci/create-ns 'cljs-time.core nil))
(def coerce-ns (sci/create-ns 'cljs-time.coerce nil))
(def format-ns (sci/create-ns 'cljs-time.format nil))

(def core-namespace
  "Only now is used by logseq.graph-parser"
  {'now (copy-var t/now core-ns)
   'date-time (copy-var t/date-time core-ns)
   'year (copy-var t/year core-ns)
   'month (copy-var t/month core-ns)
   'day (copy-var t/day core-ns)})

(def coerce-namespace
  "Only to-long is used by logseq.graph-parser"
  {'to-long (copy-var tc/to-long coerce-ns)
   'from-long (copy-var tc/from-long coerce-ns)})

(def format-namespace
  "All fns in this ns are used by logseq.graph-parser"
  {'unparse (copy-var tf/unparse format-ns)
   'parse (copy-var tf/parse format-ns)
   'formatter (copy-var tf/formatter format-ns)})

(defn init []
  (nbb/register-plugin!
   ::cljs-time
   {:namespaces {'cljs-time.core   core-namespace
                 'cljs-time.coerce coerce-namespace
                 'cljs-time.format format-namespace}}))
