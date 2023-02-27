#!/usr/bin/env node

import { loadFile } from '@logseq/nbb-logseq'
import { fileURLToPath } from 'url';
import { dirname, resolve } from 'path';

const __dirname = fileURLToPath(dirname(import.meta.url));
const { main } = await loadFile(resolve(__dirname, 'graph_ast.cljs'));

// Expects to be called as node X.js ...
const args = process.argv.slice(2)
main.apply(null, args);
