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
        (vec
         (flatten
          (map-indexed
           (fn [_ item]
             (if (= (get item :id) (get position :id))
               (if (= (get position :insert) :before)
                 [block item]
                 [item block])
               item)) blocks)))))

(defn block<-blocks
  "Takes an input of `blocks` from which it removes whatever
  block is occupying space on given `index`, and returns the
  result."
  [blocks id]
  (vec
   (remove nil?
           (map-indexed
            (fn [_ itm]
              (when-not (= (get itm :id) id)
                itm)) blocks))))

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

(defn find-by-predicate [predicate collection]
  (first (filter predicate collection)))

(defn update-by-predicate [predicate key-value collection]
  (let [key (key (first key-value))
        value (val (first key-value))]
    (vec (map (fn [i]
                (if (predicate i)
                  (assoc i key value)
                  i)) collection))))

(defn edn->json [edn]
  (.stringify js/JSON (clj->js edn)))

(defn edn->js [edn]
  (clj->js edn))

(defn json->edn [json]
  (js->clj (.parse js/JSON json) :keywordize-keys true))

(defn js->edn [js]
  (js->clj js :keywordize-keys true))