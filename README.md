# Blocko

Blocko is a block-based WYSIWYG editor written in ClojureScript and compiled to JavaScript. Currently, Blocko is not yet production ready, so use at your own risk.

![Animated gif of Blocko in action](https://github.com/askonomm/blocko/blob/master/demo.gif?raw=true)

## Development

To develop Blocko simply run `./build.sh dev`, which will then compile to `public/js/blocko.js` a development version of Blocko that also auto-reloads as you make changes. After that is done, open `public/index.html` in your browser and have fun!

Once you're done with development and want to get production version run `./build.sh release` and check inside `dist` for a brand new `blocko.js` and a `blocko.css` file.
