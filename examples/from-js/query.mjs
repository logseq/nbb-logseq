import { loadString } from '@logseq/nbb-logseq'
import { exit } from 'process';

// Expects to be called as `node query.mjs ...`
const args = process.argv.slice(2);
if (args.length < 2) {
  console.log("Usage: $0 GRAPH-DIR QUERY");
  exit(1);
};

// Evaluate cljs inline
const results = await loadString(`
  (ns query
    (:require [logseq.graph-parser.cli :as gp-cli]
              [logseq.db.file-based.rules :as file-rules]
              [logseq.db.frontend.rules :as rules]
              [datascript.core :as d]
              [clojure.edn :as edn]))

  (defn- main [graph-dir query*]
    (let [{:keys [conn]} (gp-cli/parse-graph graph-dir {:verbose false})
          query (into query* [:in '$ '%]) ;; assumes no :in are in queries
          results (map first (d/q query @conn (rules/extract-rules file-rules/query-dsl-rules)))]
      (clj->js results)))

  (main "${args[0]}" '${args[1]})
`);

console.log(JSON.stringify(results, 0, 2));
