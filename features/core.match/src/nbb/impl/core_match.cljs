(ns nbb.impl.core-match
  (:require [sci.core :as sci]
            ;; self-hostable core.match (e.g. abbinare)
            [cljs.core.match :as m]
            [nbb.core :as nbb]))

(def m-ns (sci/create-ns 'cljs.core.match nil))

;; SCI macro: first two args are &form and &env
;; Macro is mostly a copy of https://github.com/clojure/core.match/blob/753e8a30de9c487ceba8ff7d39435bf87edff505/src/main/clojure/cljs/core/match.clj#L19-L32
(defn ^:sci/macro match
  [_&form _&env vars & clauses]
  (let [[vars clauses]
        (if (vector? vars)
          [vars clauses]
          [(vector vars)
           (mapcat (fn [[c a]]
                     [(if (not= c :else) (vector c) c) a])
                   (partition 2 clauses))])]
    ;; Unlike original macro, this one doesn't have a *clojurescript* binding. Dunno if it's needed
    (binding [m/*line*   (-> _&form meta :line)
              m/*locals* (dissoc (:locals _&env) '_)
              m/*warned* (atom false)]
      (m/clj-form vars clauses))))

(def core-namespace
  {'match (sci/copy-var match m-ns)
   'backtrack (sci/copy-var m/backtrack m-ns)
   'satisfies-ILookup? #(satisfies? ILookup %)})

(def config {:namespaces {'cljs.core.match core-namespace}})

(defn init []
  (nbb/register-plugin! ::core-match config))
