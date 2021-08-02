(ns blocko.blocks.heading
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.styles :as styles]))

(def focus-el-selector "textarea")

(defn- create-block!
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

(defn- delete-block!
  "Checks if the content is empty and if it is, byebye block.
  It does dispatch a special event however, which tries to 
  find a block before this one - and then focus in it, for that smooth
  user experience sweetness."
  [id event]
  (.preventDefault event)
  (dispatch [:delete-block-and-focus-on-previous id]))

(defn- on-key-down!
  "Use case 1: 
  
  Detect if the user pressed the `enter` key or not. If
  the user did, we want to disable the default behaviour, which
  usually is creating a new line, and instead create a new 
  paragraph block below it.
  
  Use case 2:
  
  Detect if the user pressed the `backspace` key. if the the 
  user did, and if there is no more `content`, delete the block."
  [id event]
  (cond (or (= "Enter" (.-key event))
            (= 13 (.-keyCode event)))
        (create-block! id event)

        (and (empty? (.-value (.-target event)))
             (or (= "Backspace" (.-key event))
                 (= 8 (.-keyCode event))))
        (delete-block! id event)))

(defn- on-input!
  "Trigged during typing, updates the content of the block as well
  as the height of the textarea."
  [id height event]
  (dispatch
   [:update-heading-block
    {:id id
     :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn- render
  "Renders the DOM output of the paragraph block and hooks to it
  many of its necessary events."
  [block height]
  [:textarea
   {:style (merge (styles/style :heading-block-content) {:height (str @height "px")})
    :ref (fn [el] (when el (reset! height (.-scrollHeight el))))
    :default-value (get block :content)
    :placeholder "Start writing a heading ..."
    :on-key-down #(on-key-down! (get block :id) %)
    :on-focus #(dispatch [:set-active-block (get block :id)])
    :on-input #(on-input! (get block :id) height %)}])

(defn block [block]
  (let [height (r/atom 30)]
    (render block height)))