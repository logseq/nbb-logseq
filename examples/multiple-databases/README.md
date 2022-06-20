## Description

This example demonstrates querying across multiple logseq graphs. Datascript is
capable of querying across multiple databases as [these tests demonstrate](https://github.com/tonsky/datascript/blob/6de343b1b3aecb95c21fbe46384face3f99b989b/test/datascript/test/query_pull.cljc#L60-L80).

## Setup

* Install node.js >= 16.9 and yarn.
  * This minimum version of node is needed in order to use corepack and run this repo under yarn 3.
* Run `yarn install` to install npm dependencies.

## Usage

This script clones and caches each new git repo it encounters. After this
initial caching, most queries return in 1-2s.

Let's start by querying https://github.com/QWxleA/ulysses-logseq and https://github.com/pengx17/knowledge-garden:

```
# Run default query
$ yarn multiple-dbs https://github.com/QWxleA/ulysses-logseq https://github.com/pengx17/knowledge-garden
Graph https://github.com/QWxleA/ulysses-logseq is already cloned
Graph https://github.com/pengx17/knowledge-garden is already cloned
([{:block/name "waiting"}]
 [{:block/name "a"}]
 [{:block/name "favorites"}]
 [{:block/name "readme"}]
 [{:block/name "card"}]
 [{:block/name "doing"}]
 [{:block/name "later"}]
 [{:block/name "b"}]
 [{:block/name "todo"}]
 [{:block/name "done"}]
 [{:block/name "c"}]
 [{:block/name "wait"}]
 [{:block/name "contents"}]
 [{:block/name "in-progress"}]
 [{:block/name "now"}])

 # The script can also take an explicit query
 $ yarn multiple-dbs https://github.com/QWxleA/ulysses-logseq https://github.com/pengx17/knowledge-garden '[:find ?n :in $1 $2 :where [$1 ?b :block/name ?n] [$2 ?b2 :block/name ?n]]'
Graph https://github.com/QWxleA/ulysses-logseq is already cloned
Graph https://github.com/pengx17/knowledge-garden is already cloned
#{["waiting"] ["wait"] ["doing"] ["done"] ["b"] ["contents"] ["later"]
  ["favorites"]
  ["c"] ["readme"] ["todo"] ["in-progress"] ["card"] ["now"] ["a"]}
```

Unfortunately those results aren't too exciting as they don't have much knowledge overlap. Predictably the pages they have in common are the default Logseq ones.

Now let's try querying https://github.com/Xuanwo/Xuanwo and https://github.com/pengx17/knowledge-garden:

```
# Both of these graphs use :type properties so let's see what each graph has there
# Types from first graph:
$ yarn multiple-dbs https://github.com/Xuanwo/Xuanwo https://github.com/pengx17/knowledge-garden '[:find ?val :in $1 $2 :where [$1 ?b :block/properties ?bp] [(get ?bp :type) ?val]]'
Graph https://github.com/Xuanwo/Xuanwo is already cloned
Graph https://github.com/pengx17/knowledge-garden is already cloned
#{["Iteration"] [#{"Database"}] [#{"Product"}] [#{"Iteration"}]
  [#{"Paper"}]
  [#{"Project"}]
  [#{"Product" "Linux"}] [#{"Project" "Database"}] [#{"Blog"}]}

# Types from second graph:
$ yarn multiple-dbs https://github.com/Xuanwo/Xuanwo https://github.com/pengx17/knowledge-garden '[:find ?val :in $1 $2 :where [$2 ?b :block/properties ?bp] [(get ?bp :type) ?val]]'
Graph https://github.com/Xuanwo/Xuanwo is already cloned
Graph https://github.com/pengx17/knowledge-garden is already cloned
#{["logseq theme"] [#{"blogpost"}] ["blogpost"]}

# What types overlap?
$ yarn multiple-dbs https://github.com/Xuanwo/Xuanwo https://github.com/pengx17/knowledge-garden '[:find ?val :in $1 $2 :where [$1 ?b :block/properties ?bp] [(get ?bp :type) ?val] [$2 ?b2 :block/properties ?bp2] [(get ?bp2 :type) ?val]]'
Graph https://github.com/Xuanwo/Xuanwo is already cloned
Graph https://github.com/pengx17/knowledge-garden is already cloned
#{}
```

Unfortunately these graphs don't have overlapping types but maybe there are two graphs out there that do share structured knowledge?
