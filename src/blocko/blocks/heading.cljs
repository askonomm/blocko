(ns blocko.blocks.heading
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.styles :as styles]))

(defn on-key-press [index event]
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

(defn on-input [index height event]
  (dispatch
   [:update-heading-block
    {:position index
     :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn block [id index block]
  (let [height (r/atom 30)]
    (fn []
      [:textarea
       {:style (merge styles/heading-block-content {:height (str @height "px")})
        :default-value (get block :content)
        :placeholder "Start writing a heading ..."
        :ref nil #_(fn [el] (when el (reset! height (.-scrollHeight el))))
        :on-key-press #(on-key-press index %)
        :on-focus #(dispatch [:set-active-block id])
        :on-input #(on-input index height %)}])))