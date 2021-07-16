# Blocko

Blocko is a block-based WYSIWYG editor written in ClojureScript and compiled to JavaScript. Currently, Blocko is not yet production ready, so use at your own risk.

![Animated gif of Blocko in action](https://github.com/askonomm/blocko/blob/master/demo.gif?raw=true)

## Install

### NPM

1. Run: `npm i blocko-editor`
2. Import it: `import blocko from 'blocko';`

### Browser

1. Download the latest [release](https://github.com/askonomm/blocko/releases)
2. Include `blocko.css` and `blocko.js` in your HTML

## Usage

```javascript
blocko.core.init({
    container: '#editor',
    initialContent: [],
    onChange: (content) => {
        // store `content` in your database here.
    }
});
```

## API

- `container`: any JS element that can be targeted via `querySelector`
- `initialContent`: a JS or JSON object representing the data
- `onChange`: a callback function called when content changes

## Development

To develop Blocko simply run `./build.sh dev`, which will then compile to `public/js/blocko.js` a development version of Blocko that also auto-reloads as you make changes. After that is done, open `public/index.html` in your browser and have fun!

Once you're done with development and want to get production version, then:
- To get the browser production build, run `./build.sh release-browser` and check inside `dist/browser` for a brand new `blocko.js` and a `blocko.css` file.
- To get the NPM production build, run `./build.sh release-npm` and check inside `dist/npm` for a brand new `blocko.js` and a `blocko.css` file. Note that you have to import the CSS file in your project manually.
 