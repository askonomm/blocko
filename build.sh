#!/bin/bash

if [ "$1" == "dev" ]; then
    buildcmd="npx shadow-cljs watch dev"
    $buildcmd
fi

if [ "$1" == "release-browser" ]; then
    buildcmd="npx shadow-cljs release prod-browser"
    $buildcmd
    copycsscmd="cp public/blocko.css dist/browser"
    $copycsscmd
fi

if [ "$1" == "release-npm" ]; then
    buildcmd="npx shadow-cljs release prod-npm"
    $buildcmd
    copycsscmd="cp public/blocko.css dist/npm"
    $copycsscmd
fi

if [ "$1" == "prepare-npm-dist" ]; then
    copypackagecmd="cp npm-package.json dist/npm"
    $copypackagecmd
    renamepackagecmd="mv dist/npm/npm-package.json dist/npm/package.json"
    $renamepackagecmd
    copyreadmecmd="cp README.md dist/npm"
    $copyreadmecmd
fi