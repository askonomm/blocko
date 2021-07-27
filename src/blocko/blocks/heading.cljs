(ns blocko.blocks.heading
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.styles :as styles]))

(def focus-el-selector "textarea")

(defn on-key-press [id event]
  (when (or (= "Enter" (.-key event))
            (= 13 (.-keyCode event)))
    (.preventDefault event)
    (let [new-block-id (str (random-uuid))]
      (dispatch
       [:add-block
        {:position {:id id
                    :insert :after}
         :block {:id new-block-id
                 :type "paragraph"
                 :content ""}}])
      (dispatch
       [:focus
        {:id new-block-id
         :where :beginning}]))))

(defn on-input [id height event]
  (dispatch
   [:update-heading-block
    {:id id
     :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn block [id block]
  (let [height (r/atom 30)]
    (fn []
      [:textarea
       {:style (merge (styles/style :heading-block-content) {:height (str @height "px")})
        :ref (fn [el] (when el (reset! height (.-scrollHeight el))))
        :default-value (get block :content)
        :placeholder "Start writing a heading ..."
        :on-key-press #(on-key-press id %)
        :on-focus #(dispatch [:set-active-block id])
        :on-input #(on-input id height %)}])))