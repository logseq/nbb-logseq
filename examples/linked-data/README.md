## Description

These examples demonstrate how Logseq graphs can take part in the [linked
data](https://en.wikipedia.org/wiki/Linked_data) ecosystem. Logseq's properties
provide the necessary structure for users to define their own ontologies and opt
in to existing ones.

## Setup

* Install node.js >= 16 and yarn.
* Run `yarn install` to install npm dependencies.
* Install [babashka](https://github.com/babashka/babashka)

## Usage

### Write rdf

The [write_rdf.cljs](write_rdf.cljs) script converts a subset of a Logseq graph
to an rdf file. For example:

```bash
$ git clone https://github.com/logseq/docs
...
# By default the output format is written as turtle
$ yarn nbb-logseq write_rdf.cljs docs docs.ttl
Parsing 277 files...
Writing file docs.ttl
# The output format can be changed with the :format config.
# For example, if :format is changed to "n-triples":
$ yarn nbb-logseq write_rdf.cljs docs docs.nt
Parsing 277 files...
Writing file docs.nt
```

With an rdf file, you can setup a [SPARQL](https://en.wikipedia.org/wiki/SPARQL)
endpoint so anyone can query your data. [Dydra](https://dydra.com/) provides
this service for free. For example, you can query [logseq's docs
here](https://dydra.com/cldwalker/logseq-docs/@query). See
https://github.com/logseq/docs/tree/master/script for an up to date version of
this script.
