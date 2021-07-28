(ns blocko.blocks.paragraph
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [blocko.utils :as utils]
   [blocko.styles :as styles]))

(def focus-el-selector ".blocko-block--paragraph-content[data-editable='true']")

(defn create-block!
  "Creates a new block and focuses the cursor in it."
  [id event]
  (.preventDefault event)
  (let [new-block-id (str (random-uuid))]
    (dispatch [:add-block
               {:position {:id id
                           :insert :after}
                :block {:id new-block-id
                        :type "paragraph"
                        :content ""}}])
    (dispatch [:set-focus
               {:id new-block-id
                :where :beginning}])))

(defn delete-block!
  "Checks if the content is empty and if it is, byebye block.
  It does dispatch a special event however, which tries to 
  find a block before this one - and then focus in it, for that smooth
  user experience sweetness."
  [id event]
  (.preventDefault event)
  (dispatch [:delete-block-and-focus-on-previous id]))

(defn on-key-down!
  "Use case 1: 
  
  Detect if the user pressed the `enter` key or not. If
  the user did, we want to disable the default behaviour, which
  usually is creating a new line, and instead create a new 
  paragraph block below it.
  
  Use case 2:
  
  Detect if the user pressed the `backspace` key. if the the 
  user did, and if there is no more `content`, delete the block."
  [id content event]
  (cond (or (= "Enter" (.-key event))
            (= 13 (.-keyCode event)))
        (create-block! id event)

        (and (empty? content)
             (or (= "Backspace" (.-key event))
                 (= 8 (.-keyCode event))))
        (delete-block! id event)))

(defn on-input!
  "Whenever a key is pressed, we want to update the `content-state` atom 
  so that whenever a `on-paste` event occurs, it would take correct 
  content as the basis of which to calculate where to paste the clip.
  
  Reason being that whenever you type new text into the `contentEditable`, 
  even though we do update the global state, it doesn't really update 
  that component's inner `content` atom, and so when a paste 
  happens it only takes into account the initial state of it, and not the 
  new one, resulting in cursor placement then going wrong and setting in 
  the beginning of the text instead of the end of the paste location, 
  because the pasted index is further away than the actual content has 
  indexes.
  
  Oh, almost forgot, we also update the `caret-location`, so that 
  the component would trigger caret positioning (which it does only if 
  the `caret-location` is not `nil` and the `content` is as 
  long or longer than `caret-location`). This is needed because 
  after updating `content`, the component re-renders, and the caret
  location is lost and we want to set it in the right place again."
  [content caret-location id event]
  (let [new-content (utils/parse-html (.-innerHTML (.-target event)))]
    (reset! content new-content)
    (reset! caret-location (count new-content))
    (dispatch-sync
     [:update-paragraph-block
      {:id id
       :content new-content}])))

(defn on-paste!
  "Prevents the default behaviour of a paste event from happening, and 
  instead reads the contents of the clipboard, parses it (to remove any
  horrible mark-up that might come with it), and then inserts it at the
  desired position inside and updates the `content` atom with the
  new content, as well as updates the `caret-location-state` atom with 
  the new caret position which is what the caret location was when 
  pasting + the length of the pasted content.
  
  We update the `caret-location` atom so that we could do caret 
  placement on a `:component-did-update` event, because otherwise the 
  caret placement would break on paste."
  [content caret-location event]
  (.preventDefault event)
  (let [selection (.getSelection js/window)
        caret-offset (.-anchorOffset selection)]
    (if-let [clip (.getData (.-clipboardData event) "text/plain")]
      (let [pasted-content (utils/parse-html clip)
            new-content (utils/string->string @content pasted-content caret-offset)
            new-caret-location (+ caret-offset (count pasted-content))]
        (reset! content new-content)
        (reset! caret-location new-caret-location))
      (.then
       (.readText (.-clipboard js/navigator))
       (fn [clip]
         (let [pasted-content (utils/parse-html clip)
               new-content (utils/string->string @content pasted-content caret-offset)
               new-caret-location (+ caret-offset (count pasted-content))]
           (reset! content new-content)
           (reset! caret-location new-caret-location)))))))

(defn place-caret!
  "Places a caret at the desired `caret-location` position
  inside the `ref` element. But only does it if the length of 
  `content` is the same or exceeds `caret-location`. This 
  is because we cannot place a caret in an index that does not exist i.e
  is out of range.
  
  Once the caret has been placed in its position, we reset the 
  `caret-location` to `nil`, so that this placement would not occur 
  on every render, but only when we set the `caret-location` to 
  something other than `nil` (and, like said before, `content` 
  either is the same or exceeds `caret-location`).
  "
  [ref content caret-location]
  (utils/focus-block-in-position! @content @ref @caret-location)
  (reset! caret-location nil))

(defn on-placeholder-click!
  "When a user clicks the placeholder, we want to set the `focus` 
  state to `true` (which makes the placeholder disappear) and also
  dispatch a `:focus-block` event that actually makes sure the cursor
  ends up focused within the paragraph `contentEditable` as well."
  [focus id]
  (reset! focus true)
  (dispatch [:set-focus
             {:id id
              :where :end}]))
(defn render
  "Renders the actual DOM output of the paragraph block and hooks to it
  many of its necessary events."
  [{:keys [id ref focus content caret-location]}]
  [:<>
   (when (and (empty? @content)
              (nil? @focus))
     [:div.blocko-block--paragraph-content
      {:style (styles/style :paragraph-block-content-empty)
       :on-click #(on-placeholder-click! focus id)}
      "Start writing a paragraph ..."])
   [:div.blocko-block--paragraph-content
    {:style (styles/style :paragraph-block-content)
     :data-editable true
     :contentEditable true
     :ref (fn [el] (reset! ref el))
     :on-focus #(dispatch [:set-active-block id])
     :on-blur #(when (empty? @content) (reset! focus nil))
     :on-key-down #(on-key-down! id @content %)
     :on-input #(on-input! content caret-location id %)
     :on-paste #(on-paste! content caret-location %)
     :dangerouslySetInnerHTML {:__html @content}}]])

(defn block [id block]
  (let [ref (r/atom nil)
        content (r/atom (get block :content))
        caret-location (r/atom nil)
        focus (r/atom nil)]
    (r/create-class
     {:component-did-update
      #(place-caret! ref content caret-location)
      :reagent-render
      #(render {:id id
                :ref ref
                :focus focus
                :content content
                :caret-location caret-location})})))
