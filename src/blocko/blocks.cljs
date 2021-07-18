(ns blocko.blocks
  (:require
   ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
   ["@fortawesome/free-solid-svg-icons" :refer (faTrash)]
   [reagent.core :as r]
   [re-frame.core :refer [dispatch subscribe]]
   [blocko.events]
   [blocko.blocks.paragraph :as blocks.paragraph]
   [blocko.blocks.heading :as blocks.heading]
   [blocko.blocks.add :as blocks.add]
   [blocko.utils :as utils]
   [blocko.styles :as styles]))

(defn focus! [{:keys [id _]} blocks]
  (when-let [block-el (.querySelector js/document (str ".blocko-block[data-id='" id "']"))]
    (let [block (utils/find-by-predicate #(= (:id %) id) blocks)]
      (cond (= "paragraph" (get block :type))
            (.focus (.querySelector block-el ".blocko-block--paragraph-content[data-editable='true']"))
            (= "heading" (get block :type))
            (.focus (.querySelector block-el "textarea")))
      (dispatch [:focus-block nil]))))

(defn content [id index block]
  (cond
    (= "paragraph" (get block :type))
    (blocks.paragraph/block id index block)
    (= "heading" (get block :type))
    (blocks.heading/block id index block)
    :else nil))

(defn controls [id]
  [:div.blocko-controls {:style styles/controls}
   [:div.blocko-control
    {:style styles/control
     :on-click #(dispatch [:delete-block id])}
    [:> FontAwesomeIcon {:icon faTrash}]]])

(defn block [index block]
  (let [active-block (subscribe [:active-block])
        {:keys [id type]} block]
    (fn []
      [:div.blocko-block
       {:style styles/block
        :class type
        :data-id id}
       (when (= id @active-block)
         [controls id])
       [content id index block]])))

(defn blocks []
  (let [state (r/atom [])]
    (r/create-class
     {:component-did-update
      (fn [this _]
        (let [new-argv (into {} (rest (r/argv this)))
              new-blocks (get new-argv :blocks)
              block-focus (get new-argv :block-focus)]
          (reset! state new-blocks)
          (when block-focus
            (focus! block-focus new-blocks))))
      :reagent-render
      (fn []
        [:div.blocko {:style styles/container}
         [blocks.add/block 0]
         (map-indexed
          (fn [index item]
            ^{:key (get item :id)}
            [:<>
             [block index item]
             [blocks.add/block (+ index 1)]])
          @state)])})))