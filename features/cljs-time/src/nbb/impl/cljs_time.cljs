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
  "Only now, days, weeks, months, years, today, minus and plus are used by logseq.graph-parser"
  {'now (copy-var t/now core-ns)
   'date-time (copy-var t/date-time core-ns)
   'year (copy-var t/year core-ns)
   'years (copy-var t/years core-ns)
   'month (copy-var t/month core-ns)
   'months (copy-var t/months core-ns)
   'weeks (copy-var t/weeks core-ns)
   'day (copy-var t/day core-ns)
   'days (copy-var t/days core-ns)
   'today (copy-var t/today core-ns)
   'minus (copy-var t/minus core-ns)
   'plus (copy-var t/plus core-ns)
   'to-default-time-zone (copy-var t/to-default-time-zone core-ns)})

(def coerce-namespace
  "Only to-long is used by logseq.graph-parser"
  {'to-long (copy-var tc/to-long coerce-ns)
   'from-long (copy-var tc/from-long coerce-ns)
   'to-local-date (copy-var tc/to-local-date coerce-ns)
   'to-local-date-time (copy-var tc/to-local-date-time coerce-ns)})

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
