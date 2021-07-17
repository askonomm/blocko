#!/bin/bash

if [ "$1" == "dev" ]; then
    buildcmd="npx shadow-cljs watch dev"
    $buildcmd
fi

if [ "$1" == "release" ]; then
    buildcmd="npx shadow-cljs release prod"
    $buildcmd
    copycsscmd="cp public/blocko.css dist"
    $copycsscmd
fi