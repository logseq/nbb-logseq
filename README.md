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

For examples that are specific to `nbb-logseq`, see the [examples directory](examples).

## Projects using nbb-logseq

* https://github.com/logseq/graph-validator - github action
* https://github.com/cldwalker/logseq-query - commandline tool
* https://github.com/logseq/bb-tasks/tree/main/src/logseq/bb_tasks/nbb - bb tasks that use nbb-logseq

## Versioning

`nbb-logseq` follows the same versioning as `nbb`. In other words, an 0.4.0 for
`nbb-logseq` provides `nbb` 0.4.0 with the additional libraries.

## Contributing

Example contributions are welcome. For feature contributions, please discuss
them first as this is a low level library that will be relied on by multiple
CLIs.

## LICENSE

See LICENSE.md
