## Description

This example is a generic HTTP service to query a Logseq graph using
https://fly.io/. This app represents a new class of applications that are
independent of the Logseq app and only require your Logseq graph to function.
These applications use [these
libraries](https://github.com/logseq/logseq/tree/master/deps) to provide a
read-only database. This database is the same database as the Logseq app and
thus is capable of running _any_ of the same queries. While this example is an
HTTP service, these libraries can be used to be build _any_ Node.js application
e.g. a slackbot or a voice assistant. This example service should be able to run
for free for most personal graphs. Give it a spin!

## Demo

https://frosty-cherry-9258.fly.dev/ is a demo that queries [Logseq's
docs](https://github.com/logseq/docs).

Any query run by this service has an associated link. For example, this basic query
```clojure
[:find (pull ?b [:block/name]) :where [?b :block/name]]
```
has its results at [this url](https://frosty-cherry-9258.fly.dev/q?q=%5B%3Afind+%28pull+%3Fb+%5B%3Ablock%2Fname%5D%29+%3Awhere+%5B%3Fb+%3Ablock%2Fname%5D%5D).

Queries can use the same rules as the Logseq app. For example, query all done tasks

```clojure
[:find (pull ?b [*])
 :where (task ?b #{"DONE"})]
```

and see [the results](https://frosty-cherry-9258.fly.dev/q?q=%5B%3Afind+%28pull+%3Fb+%5B*%5D%29%0D%0A+%3Awhere+%28task+%3Fb+%23%7B%22DONE%22%7D%29%5D).

Query results return as EDN and thus can be processed locally. For example, let's look
at just the block properties from the last query:

```shell
$ curl https://frosty-cherry-9258.fly.dev/q\?q\=%5B%3Afind+%28pull+%3Fb+%5B\*%5D%29%0D%0A+%3Awhere+%28task+%3Fb+%23%7B%22DONE%22%7D%29%5D | \
bb '(->> *input* (map :block/properties))'
({:done 1614350275750} nil {:now 1603457565500, :done 1603457583299} nil {:created-at 1609233518468, :updated-at 1609233678857} {:todo 1612237041309, :done 1612237041727})
```

## Setup

To try this service locally on your Logseq graph:

1. Install [babashka](https://github.com/babashka/babashka) >= 0.8.156.
2. Install node.js >= 16.3.1.
3. Run `npm install` to install npm dependencies.
4. Clone your graph: `git clone YOUR-GRAPH resources/graph`
  * If you don't have a graph, try the Logseq docs
  graph with `git clone --depth 1 https://github.com/logseq/docs resources/graph.`
5. Start the web server: `npm start`

Go to http://localhost:8092/ and query away!

## Deployment

To deploy a https://fly.io app:
* [Install flyctl](https://fly.io/docs/getting-started/installing-flyctl/)
* [Login to fly.io](https://fly.io/docs/getting-started/log-in-to-fly/).
  * If you don't have an account, you will need to signup and provide a credit card. As long as your graph is smaller than https://github.com/logseq/docs and are using it mostly for personal use, you should be able to run this app for free. See https://fly.io/docs/about/pricing/ for more.
* Be sure you have run setup steps 1, 3 and 5.
* Run `flyctl launch` to create your own application. The `fly.toml` should change to have your app's name. Subsequent deploys are with `flyctl deploy`.

## LICENSE
Same license as nbb-logseq.

## Credits
* Thanks to https://github.com/babashka/nbb/tree/main/doc/fly_io, which demonstrated a fly.io service is possible on nbb.
* Thanks to [this Codemirror demo](https://codemirror.net/5/mode/clojure/index.html) for providing an easy, datalog friendly input.
