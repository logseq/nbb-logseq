# Examples

## query.cljs

This example assumes you have Logseq installed as a desktop app and have added
the [docs graph](https://github.com/logseq/docs).

```clojure
$ nbb-logseq query.cljs docs '[:find (pull ?b [*]) :where [?b :block/marker]]'
nbb-logseq query.cljs docs '[:find (pull ?b [*]) :where [?b :block/marker]]'
({:block/uuid #uuid "6238a3d4-4d33-43ab-aca3-463ec803da87",
  :block/priority "A",
  :block/left {:db/id 6360},
  :block/refs [{:db/id 5} {:db/id 14}],
  :block/children #{},
  :block/meta
  {:timestamps [], :properties [], :start-pos 774, :end-pos 824},
  :block/format :markdown,
  :block/level 3,
  :block/content "LATER [#A] research balalah is a top priority",
  :db/id 6361,
  :block/path-refs [{:db/id 5} {:db/id 14} {:db/id 1969}],
  :block/parent {:db/id 6360},
  :block/unordered true,
  :block/page {:db/id 1969},
  :block/marker "LATER"}
...

# Print all page names
$ nbb-logseq query.cljs docs "[:find ?n :where [?b :block/name ?n]]"
("60ab3eb7-2fb1-4148-af2b-9ba2319ef5b6"
 "youtube timestamp"
 "enhancement"
 "may 31st, 2020"
 ...
```

## fly.io http server example

See [its README](fly-io/README.md).

## multiple databases example

See [its README](multiple-databases/README.md).

## Additional repositories using nbb-logseq

* https://github.com/logseq/graph-validator - github action
* https://github.com/cldwalker/logseq-query - commandline tool
