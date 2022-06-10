## Description

This example demonstrates deploying a Logseq graph as a web service using https://fly.io/. The web service can handle [datascript](https://github.com/tonsky/datascript) queries and return EDN results. Queries can use Logseq's rules.

## Demo

The [docs graph](https://github.com/logseq/docs) is deployed as a service at
https://frosty-cherry-9258.fly.dev/.
For a basic query like
```clojure
[:find (pull ?b [:block/name]) :where [?b :block/name]]
```
click on [this url](https://frosty-cherry-9258.fly.dev/q?q=%5B%3Afind+%28pull+%3Fb+%5B%3Ablock%2Fname%5D%29+%3Awhere+%5B%3Fb+%3Ablock%2Fname%5D%5D).

TODO

## Setup

To try this locally on your Logseq graph:

* Install [babashka](https://github.com/babashka/babashka).
* Run `npm install` to install npm dependencies.
* Run `bb install` to install cljs dependencies.
* Clone your graph: `git clone
  YOUR-GRAPH resources/graph`
  * If you don't have a graph, try the Logseq docs
  graph with `git clone --depth 1 https://github.com/logseq/docs resources/graph.`
* Start the web server: `npm start`

To try a query in your terminal, run `bb dev:query "[:find (pull ?b [:block/name]) :where [?b :block/name]]"`

## Deployment

TODO

## Additional Links
* https://github.com/babashka/nbb/tree/main/doc/fly_io is the original tutorial this one is based off.
