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

(defn focus!
  "Attempts to capture the DOM element for the block with a given `id`,
  and to find the actual block data itself as well by a given `id`, which
  it then tries to focus in according to `where`. After which, it will
  set the `:focus` state to `nil`."
  [{:keys [id where]} blocks]
  (when-let [block-el (.querySelector js/document (str ".blocko-block[data-id='" id "']"))]
    (let [block (utils/find-by-predicate #(= (:id %) id) blocks)
          content (get block :content)]
      (cond (= "paragraph" (get block :type))
            (let [el (.querySelector block-el blocks.paragraph/focus-el-selector)]
              (utils/focus-block-in-position! content el where))
            (= "heading" (get block :type))
            (let [el (.querySelector block-el blocks.heading/focus-el-selector)]
              (utils/focus-block-in-position! content el where)))
      (dispatch [:set-focus nil]))))

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
  (let [state (r/atom [])
        focus (subscribe [:focus])]
    (r/create-class
     {:component-did-update
      (fn [this _]
        (let [new-argv (into {} (rest (r/argv this)))
              new-blocks (get new-argv :blocks)]
          (reset! state new-blocks)
          (when @focus
            (focus! @focus new-blocks))))
      :reagent-render
      (fn []
        [:div.blocko {:style (styles/style :container)}
         [blocks.add/block {:position :beginning}]
         (map
          (fn [item]
            ^{:key (get item :id)}
            [:<>
             [block item]
             [blocks.add/block {:position {:id (get item :id)
                                           :insert :after}}]])
          @state)])})))