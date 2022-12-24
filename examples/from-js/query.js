import { loadFile } from '@logseq/nbb-logseq'

// destructure JS object returned from .cljs file:
const { queryGraph } = await loadFile('query.cljs')

// Expects to be called as node query.js ...
const args = process.argv.slice(2)
console.log(JSON.stringify(queryGraph(args)));
