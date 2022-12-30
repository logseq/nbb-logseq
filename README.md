## Description
### For Logseq users

`nbb-logseq` provides [easy CLJS scripting on
Node.js](https://github.com/babashka/nbb) for Logseq. Since logseq is primarily
written with [ClojureScript](https://clojurescript.org/), this scripting
environment has capabilities that are not possible in any other environment. For
example, [see here](examples/#query.cljs) for a script that queries any logseq
graph.

### For Clojure users

`nbb-logseq` is a custom version of [nbb](https://github.com/babashka/nbb) that
bundles support for datascript, datascript-transit and a couple other cljs
libraries that are useful to logseq. A good amount of the datascript API is
available as [seen by its
tests](https://github.com/babashka/nbb-features/blob/main/test/features/datascript/test_runner.cljs).
Since nbb only loads features/libraries when they are required, users can write
datascript based CLIs without concern of loading the other libraries.

## Usage

Install `nbb-logseq` from npm:

`npm install @logseq/nbb-logseq -g`

Omit `-g` for a local install.

All the usage examples from https://github.com/babashka/nbb#usage apply to
`nbb-logseq` e.g.

```clojure
$ nbb-logseq -e '(+ 1 2 3)'
6
```

## Dependency Management

`nbb-logseq` can use libraries from both NPM and ClojureScript. For NPM
libraries,  use `package.json` like you would in a Node.js project. For
ClojureScript libraries, create a `nbb.edn` file and [install
babashka](https://github.com/babashka/babashka#installation). The two main keys
a `nbb.edn` file supports are `:deps` and `:paths`. See [babashka
docs](https://book.babashka.org/#_paths_and_deps) for more about the format of
this file and those keys. See [nbb's
docs](https://github.com/babashka/nbb#dependencies) for more info about how
nbb's dependencies generally work.

### Logseq as a Dependency

Logseq's graph parser is a ClojureScript library with npm dependencies. To use
it as a dependency requires a `nbb.edn` for the ClojureScript code and a
`package.json` for the npm dependencies. For a quickstart with these files, copy
them from the [fly.io example](examples/fly-io). Note that Logseq code is fetched
via git with `nbb.edn` so a specific commit and git repository can be
specified.

## Projects using nbb-logseq

* https://github.com/logseq/graph-validator - github action
* https://github.com/cldwalker/logseq-query - commandline tool
* https://github.com/logseq/bb-tasks#logseqbb-tasksnbb - bb tasks that use nbb-logseq
* https://github.com/dom8509/logseq-to-markdown - CLI to export graph to Hugo Markdown files
* https://github.com/logseq/docs/tree/master/script - Scripts to query docs
* For more examples see the [examples directory](examples).

## Versioning

`nbb-logseq` follows the same versioning as `nbb`. In other words, an 0.4.0 for
`nbb-logseq` provides `nbb` 0.4.0 with the additional libraries.

## Contributing

Example contributions are welcome. For feature contributions, please discuss
them first as this is a low level library that will be relied on by multiple
CLIs.

## LICENSE

See LICENSE.md
