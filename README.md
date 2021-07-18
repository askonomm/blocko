# Blocko

Blocko is a block-based WYSIWYG editor written in ClojureScript meant to be used within JavaScript and ClojureScript projects. Currently, Blocko is not yet production ready, so use at your own risk.

![Animated gif of Blocko in action](https://github.com/askonomm/blocko/blob/master/demo.gif?raw=true)

## Install

### Browser

1. Download the latest [release](https://github.com/askonomm/blocko/releases)
2. Include `blocko.js` in your HTML

#### Usage

```javascript
blocko.core.init({
    container: '#editor',
    content: [],
    onChange: (content) => {
        // store `content` in your database here.
    }
});
```

#### API

- `container`: any DOM element that can be targeted via `querySelector`
- `content`: a JS or JSON object representing the data
- `onChange`: a callback function called when content changes

### ClojureScript

1. Add `[org.clojars.askonomm/blocko "0.1"]` to your dependencies
2. Since Blocko is using NPM packages, make sure to run it with Shadow-CLJS and add the following dependencies to your `package.json`:

```json
"dependencies": {
  "@fortawesome/fontawesome-svg-core": "^1.2.35",
  "@fortawesome/free-regular-svg-icons": "^5.15.3",
  "@fortawesome/free-solid-svg-icons": "^5.15.3",
  "@fortawesome/react-fontawesome": "^0.1.14",
  "react": "^17.0.2",
  "react-dom": "^17.0.2",
  "sanitize-html": "^2.4.0"
}
```

**Note:** I do try to constantly get rid of the reliance on third-party packages, or at the very least non-Clojars packages, so that at one point there wouldn't be any reliance on NPM and you could also include Blocko in a CLJS project that does not use Shadow-CLJS. Until then however, if you use something other than Shadow-CLJS I recommend you get the browser build instead and add that to your project.

#### Usage

```clojure
(ns your-app
  (:require [blocko.core :as blocko]))

(blocko/run 
  {:content []
   :on-change #(fn [content] (prn "store content in your database here"))})

```

#### API

- `content`: a vector containing the data
- `on-change`: a callback function called when content changes

## Data structure

Blocko uses a very simple data structure to define the content of the editor. It's simply an array of objects, each object representing one block, an the order that the array is in is also the order at which blocks will be displayed on the editor.

### Paragraph block

#### JSON

```json
{"id": "uuid",
 "type": "paragraph",
 "content": "..."}
```

#### EDN
```clojure
{:id "uuid",
 :type "paragraph",
 :content "..."}
```

### Heading block

#### JSON

```json
{"id": "uuid",
 "type": "heading",
 "content": "..."}
```

#### EDN
```clojure
{:id "uuid",
 :type "heading",
 :content "..."}
```

## Development

To develop Blocko simply run `./build.sh dev`, which will then compile to `public/js/blocko.js` a development version of Blocko that also auto-reloads as you make changes. After that is done, open `localhost:8000` in your browser and have fun!

Once you're done with development and want to get the production version, then:
- To get the browser production build, run `./build.sh release` and check inside `dist` for a brand new `blocko.js` file.
 