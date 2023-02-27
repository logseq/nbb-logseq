## Description

These examples demonstrate that nbb-logseq can be called from Node.js. This is
helpful for those wanting to primarily write Node.js scripts and only call to
ClojureScript as needed. For more docs also see [nbb's
examples](https://github.com/babashka/nbb#calling-nbb-from-javascript).

## Setup

* Install node.js >= 16 and yarn.
* Run `yarn install` to install npm dependencies.
* Install [babashka](https://github.com/babashka/babashka)

## Usage

The following sections describe Node.js scripts that call Logseq's graph-parser
in ClojureScript and then return control to Javascript to process the result.
These scripts use a specific version of Logseq as specified in `nbb.edn`.

### query.js

[query.js](query.js) converts the given graph directory into a database and
then runs the given query on that database. This script is able to run any
Datalog query the Logseq app can, but from the commandline. Here's how to use
this script:

```sh
# First clone an example graph like the logseq docs
$ git clone https://github.com/logseq/docs && cd docs

# Print all the page names in the graph
$ node query.js . '[:find ?n :where [?b :block/name ?n]]'
Parsing 269 files...
[
  "setting___enable journals",
...

# Print all the tasks' contents
$ node query.js . '[:find (pull ?b [:block/content]) :where (task ?b #{"TODO"})]'
Parsing 269 files...
[
  {
    "content": "TODO List browsers we support #docs"
  },
...
```

Notes about this script:
* nbb-logseq's `loadString` evaluates inline ClojureScript in a Node.js script.
  Use this for scripts that don't have much ClojureScript.
* `gp-cli/parse-graph` is the most important ClojureScript fn as it converts a
  graph into a database.
* This script is slow for larger graphs like docs. There are ways to cache graph
  parsing but this was avoided to keep this example simple.
* This script runs the datalog query and converts edn to json, `clj->js`, in
  ClojureScript. While these can be run in JS with 3rd party libraries, they are
  less complete and likely buggier versions of the ClojureScript equivalents.

### graph_ast.mjs

[graph_ast.mjs](graph_ast.mjs) parses the given graph directory and returns a map of files
and their ast trees. An ast tree is basically a data representation of a file
that identifies almost every actionable piece of text Logseq recognizes e.g. a
url, property, page reference, etc. Unlike the previous script, let's install and use it as a CLI:

```sh
$ yarn global add $PWD
# Print help to understand what arguments are available
$ logseq-graph-ast -h
Usage: $0 GRAPH-DIR [NODE-TYPE]

Valid node-types are simple-query, advanced-query, url

# Same as the above, though using the CLI
# version allows us to call it from any directory
$ node graph_ast.mjs -h
Usage: $0 GRAPH-DIR [NODE-TYPE]
...
```

Here's how to use this script:
```sh
# First clone an example graph like the logseq docs
$ git clone https://github.com/logseq/docs && cd docs

# Print all ast data in the graph. A lot of data is printed out
$ logseq-graph-ast .
Parsing 269 files...
[{:file "./journals/2021_07_19.md",
  :ast
  ([["Heading"
...

# Print all urls in the graph
$ logseq-graph-ast . url
Parsing 269 files...
["https://asciidoctor.org/docs/user-manual/#admonition",
...

# Print all simple queries in the graph
$ logseq-graph-ast . simple-query
Parsing 269 files...
["(namespace [[term]])",
...

# This script can also be invoked directly without calling graph_ast.mjs
$ yarn nbb-logseq -m graph-ast/-main
...
```

Notes about this script:
* nbb-logseq's `loadFile` evaluates a ClojureScript file in a Node.js script. This is
  useful when there is enough ClojureScript that it'd help to have it in a separate file
  for development purposes e.g. syntax highlighting.
* The `gp-cli/parse-graph` returns ast data, in addition to a database.
* In graph_ast.cljs, see `clojure.walk/postwalk` which makes it easy to walk the ast
  trees and find specific nodes easily.
