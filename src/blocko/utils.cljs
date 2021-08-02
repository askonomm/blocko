(ns blocko.utils
  (:require
   ["sanitize-html" :as sanitize-html]))

(defn block->blocks
  "Takes an input of `blocks`, into which it adds `block`, 
  depending on given `position`. If the `blocks` are empty, 
  it simply returns a vector with just the given `block` in it. 
  Otherwise, it will position it accordingly."
  [blocks block position]
  (cond (empty? blocks)
        [block]
        (= position :beginning)
        (into [block] blocks)
        (= position :end)
        (conj blocks block)
        :else
        (flatten
         (mapv
          (fn [item]
            (if (= (get item :id) (get position :id))
              (if (= (get position :insert) :before)
                [block item]
                [item block])
              item)) blocks))))

(defn block<-blocks
  "Takes an input of `blocks` from which it removes whatever
  block is occupying space with a given `id`, and returns the
  result."
  [blocks id]
  (remove nil?
          (mapv (fn [i]
                  (when-not (= (get i :id) id)
                    i)) blocks)))

(defn block-before-block
  "Gets the block previous to the block corresponding to `id` 
  within a set of given `blocks`."
  [id blocks]
  (let [current-block-index (first (keep-indexed #(when (= (:id %2) id) %1) blocks))]
    (when-not (= 0 current-block-index)
      (get (vec blocks) (- current-block-index 1)))))

(defn focus-el-in-offset!
  "For a given `el`, will attempt to set the caret position according to
  `offset`."
  [el offset]
  (let [selection (.getSelection js/window)
        range (.createRange js/document)
        first-child-node (first (.-childNodes el))]
    (if first-child-node
      (.setStart range first-child-node offset)
      (.setStart range el offset))
    (.collapse range true)
    (.removeAllRanges selection)
    (.addRange selection range)
    (.focus el)))

(defn focus-el-in-position!
  "For a given `el`, will attempt to set the caret position according to 
  `where`, which can be either `:beginning`, `:end` or an actual offset."
  [el where]
  (cond (= :beginning where)
        (.focus el)
        (= :end where)
        (let [content (if (= "div" (.toLowerCase (.-tagName el)))
                        (.-innerHTML el)
                        (.-value el))
              offset (count content)]
          (focus-el-in-offset! el offset))
        :else
        (focus-el-in-offset! el where)))

(defn parse-html
  "Takes in a raw string of `html`, and then removes all HTML
  elements other than a `a`, `u`, `b`, `i`. It also removes 
  any attributes the HTML elements may have, to make sure that
  when a user pastes content from something that gives along 
  formatting instructions, we'd ignore that."
  [html]
  (sanitize-html
   html
   (clj->js
    {:allowedTags ["b" "strong" "i" "em" "a" "u"]
     :allowedAttributes {"a" ["href"]}})))

(defn string->string
  "Inserts the `inserted-string` into `string` at the given
  index `index`."
  [string inserted-string index]
  (let [split-beginning (subs string 0 index)
        split-end (subs string index)]
    (str split-beginning inserted-string split-end)))

(defn string<-string
  "Removes a selection between `from-offset` and `to-offset` 
  from the given `string`."
  [string from-offset to-offset]
  (let [split-beginning (subs string 0 from-offset)
        split-end (subs string to-offset)]
    (str split-beginning split-end)))

(defn find-by-predicate
  "Finds the first item in a given `collection by a given `predicate`."
  [predicate collection]
  (first (filter predicate collection)))

(defn update-by-predicate
  "Updates item(s) in a given `collection` according to given `predicate`
  with a key-value pair given with `key-value`. "
  [predicate key-value collection]
  (let [key (key (first key-value))
        value (val (first key-value))]
    (vec (map (fn [i]
                (if (predicate i)
                  (assoc i key value)
                  i)) collection))))
