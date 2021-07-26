(ns blocko.blocks
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch subscribe]]
   [blocko.events]
   [blocko.blocks.paragraph :as blocks.paragraph]
   [blocko.blocks.heading :as blocks.heading]
   [blocko.blocks.add :as blocks.add]
   [blocko.icons :as icons]
   [blocko.utils :as utils]
   [blocko.styles :as styles]))

(defn focus-at! 
  "For a given `block`, and its `block-el`, will attempt to set
  the caret position according to `where` within the `focus-el-selector`."
  [block block-el focus-el-selector where]
  (if (= :beginning where)
    (.focus (.querySelector block-el focus-el-selector))
    (let [content (get block :content)
          selection (.getSelection js/window)
          range (.createRange js/document)
          el (.querySelector block-el focus-el-selector)
          first-child-node (first (.-childNodes el))]
      (if first-child-node
        (.setStart range first-child-node (count content))
        (.setStart range el (count content)))
      (.collapse range true)
      (.removeAllRanges selection)
      (.addRange selection range)
      (.focus (.querySelector block-el focus-el-selector)))))

(defn focus! 
  "Attempts to capture the DOM element for the block with a given `id`,
  and to find the actual block data itself as well by a given `id`, which
  it then tries to focus in according to `where`. After which, it will
  set the `:block-focus` state to `nil`."
  [{:keys [id where]} blocks]
  (when-let [block-el (.querySelector js/document (str ".blocko-block[data-id='" id "']"))]
    (let [block (utils/find-by-predicate #(= (:id %) id) blocks)]
      (cond (= "paragraph" (get block :type))
            (focus-at! block block-el blocks.paragraph/focus-el-selector where)
            (= "heading" (get block :type))
            (focus-at! block block-el blocks.heading/focus-el-selector where))
      (dispatch [:focus-block nil]))))

(defn content [id block]
  (cond
    (= "paragraph" (get block :type))
    (blocks.paragraph/block id block)
    (= "heading" (get block :type))
    (blocks.heading/block id block)
    :else nil))

(defn delete-control [id]
  (let [hover? (r/atom nil)]
    (fn []
      [:div.blocko-control
       {:style (if @hover?
                 (styles/style :control-hover)
                 (styles/style :control))
        :on-mouse-enter #(reset! hover? true)
        :on-mouse-leave #(reset! hover? nil)
        :on-click #(dispatch [:delete-block id])}
       [icons/trash (styles/style :control-icon-color) true]])))

(defn controls [id]
  [:div.blocko-controls {:style (styles/style :controls)}
   [delete-control id]])

(defn block [block]
  (let [active-block (subscribe [:active-block])
        {:keys [id type]} block]
    (fn []
      [:div.blocko-block
       {:style (styles/style :block)
        :class type
        :data-id id}
       (when (= id @active-block)
         [controls id])
       [content id block]])))

(defn blocks []
  (let [state (r/atom [])]
    (r/create-class
     {:component-did-update
      (fn [this _]
        (let [new-argv (into {} (rest (r/argv this)))
              new-blocks (get new-argv :blocks)
              block-focus (get new-argv :block-focus)]
          (reset! state [])
          (reset! state new-blocks)
          (when block-focus
            (focus! block-focus new-blocks))))
      :reagent-render
      (fn []
        [:div.blocko {:style (styles/style :container)}
         [blocks.add/block {:position :beginning}]
         (map-indexed
          (fn [_ item]
            ^{:key (get item :id)}
            [:<>
             [block item]
             [blocks.add/block {:position {:id (get item :id)
                                           :insert :after}}]])
          @state)])})))