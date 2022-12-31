#!/usr/bin/env node

import { loadFile } from '@logseq/nbb-logseq'

// destructure JS object returned from .cljs file:
const { printAst } = await loadFile('ast.cljs')

// Expects to be called as node ast.js ...
const args = process.argv.slice(2)
console.log(JSON.stringify(printAst(args), 0, 2));
