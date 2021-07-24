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
            (.focus (.querySelector block-el blocks.paragraph/focus-el-selector))
            (= "heading" (get block :type))
            (.focus (.querySelector block-el blocks.heading/focus-el-selector)))
      (dispatch [:focus-block nil]))))

(defn content [id block]
  (cond
    (= "paragraph" (get block :type))
    (blocks.paragraph/block id block)
    (= "heading" (get block :type))
    (blocks.heading/block id block)
    :else nil))

(defn controls [id]
  [:div.blocko-controls {:style styles/controls}
   [:div.blocko-control
    {:style styles/control
     :on-click #(dispatch [:delete-block id])}
    [:> FontAwesomeIcon {:icon faTrash}]]])

(defn block [block]
  (let [active-block (subscribe [:active-block])
        {:keys [id type]} block]
    (fn []
      [:div.blocko-block
       {:style styles/block
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
        [:div.blocko {:style styles/container}
         [blocks.add/block {:position :beginning}]
         (map-indexed
          (fn [_ item]
            ^{:key (get item :id)}
            [:<>
             [block item]
             [blocks.add/block {:position {:id (get item :id)
                                           :insert :after}}]])
          @state)])})))