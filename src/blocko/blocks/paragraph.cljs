(ns blocko.blocks.paragraph
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.utils :as utils]))

(defn on-key-press!
  "Detect if the user pressed the `enter` key or not. If
  the user did, we want to disable the default behaviour, which
  usually is creating a new line, and instead create a new 
  paragraph block below it."
  [index event]
  (when (or (= "Enter" (.-key event))
            (= 13 (.-keyCode event)))
    (.preventDefault event)
    (let [id (str (random-uuid))]
      (dispatch
       [:add-block
        {:position (+ index 1)
         :block {:id id
                 :type "paragraph"
                 :content ""}}])
      (dispatch
       [:focus-block
        {:id id
         :where :beginning}]))))

(defn on-input!
  "Whenever a key is pressed, we want to update the `content-state` atom 
  so that whenever a `on-paste` event occurs, it would take correct 
  content as the basis of which to calculate where to paste the clip.
  
  Reason being that whenever you type new text into the `contentEditable`, 
  even though we do update the global state, it doesn't really update 
  that component's inner `content-state` atom, and so when a paste 
  happens it only takes into account the initial state of it, and not the 
  new one, resulting in cursor placement then going wrong and setting in 
  the beginning of the text instead of the end of the paste location, 
  because the pasted index is further away than the actual content has 
  indexes.
  
  Oh, almost forgot, we also update the `caret-location-state`, so that 
  the component would trigger caret positioning (which it does only if 
  the `caret-location-state` is not `nil` and the `content-state` is as 
  long or longer than `caret-location-state`). This is needed because 
  after updating `content-state`, the component re-renders, and the caret
  location is lost and we want to set it in the right place again."
  [content-state caret-location-state index event]
  (let [content (utils/parse-html (.-innerHTML (.-target event)))]
    (reset! content-state content)
    (reset! caret-location-state (count content))
    (dispatch
     [:update-paragraph-block
      {:position index
       :content content}])))

(defn on-paste!
  "Prevents the default behaviour of a paste event from happening, and 
  instead reads the contents of the clipboard, parses it (to remove any
  horrible mark-up that might come with it), and then inserts it at the
  desired position inside and updates the `content-state` atom with the
  new content, as well as updates the `caret-location-state` atom with 
  the new caret position which is what the caret location was when 
  pasting + the length of the pasted content.
  
  We update the `caret-location-state` atom so that we could do caret 
  placement on a `:component-did-update` event, because otherwise the 
  caret placement would break on paste."
  [content-state caret-location-state event]
  (.preventDefault event)
  (.then
   (.readText (.-clipboard js/navigator))
   (fn [clip]
     (let [selection (.getSelection js/window)
           caret-location (.-anchorOffset selection)
           pasted-content (utils/parse-html clip)
           new-content (utils/string->string @content-state pasted-content caret-location)
           new-caret-location (+ caret-location (count pasted-content))]
       (reset! content-state new-content)
       (reset! caret-location-state new-caret-location)))))

(defn place-caret!
  "Places a caret at the desired `caret-location-state` position
  inside the `ref` element. But only does it if the length of 
  `content-state` is the same or exceeds `caret-location-state`. This 
  is because we cannot place a caret in an index that does not exist i.e
  is out of range.
  
  Once the caret has been placed in its position, we reset the 
  `caret-location-state` to `nil`, so that this placement would not occur 
  on every render, but only when we set the `caret-location-state` to 
  something other than `nil` (and, like said before, `content-state` 
  either is the same or exceeds `caret-location-state`).
  "
  [ref content-state caret-location-state]
  (when (and (not (nil? @caret-location-state))
             (>= (count @content-state) @caret-location-state)
             (first (.-childNodes @ref)))
    (let [selection (.getSelection js/window)
          range (.createRange js/document)]
      (.setStart range (first (.-childNodes @ref)) @caret-location-state)
      (.collapse range true)
      (.removeAllRanges selection)
      (.addRange selection range)
      (.focus @ref)
      (reset! caret-location-state nil))))

(defn render
  "Renders the actual DOM output of the paragraph block and hooks to it
  many of its necessary events."
  [id ref content-state caret-location-state index]
  [:div.paragraph-content
   {:contentEditable true
    :data-placeholder "Start writing a paragraph ..."
    :ref (fn [el] (reset! ref el))
    :on-focus #(dispatch [:set-active-block id])
    :on-key-press #(on-key-press! index %)
    :on-input #(on-input! content-state caret-location-state index %)
    :on-paste #(on-paste! content-state caret-location-state %)
    :dangerouslySetInnerHTML {:__html @content-state}}])

(defn block [id index block]
  (let [ref (r/atom nil)
        content-state (r/atom (get block :content))
        caret-location-state (r/atom nil)]
    (r/create-class
     {:component-did-update
      #(place-caret! ref content-state caret-location-state)
      :reagent-render
      #(render id ref content-state caret-location-state index)})))