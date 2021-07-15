(ns blocko.blocks.add
  (:require
   ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
   ["@fortawesome/free-solid-svg-icons" :refer (faPlus faParagraph faHeading)]
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]))

(defn add-paragraph [index block-menu]
  (dispatch
   [:add-block
    {:position index
     :block {:id (str (random-uuid))
             :type "paragraph"
             :content ""}}])
  (reset! block-menu nil))

(defn add-heading [index block-menu]
  (dispatch
   [:add-block
    {:position index
     :block {:id (str (random-uuid))
             :type "heading"
             :content ""}}])
  (reset! block-menu nil))

(defn block [index]
  (let [block-menu (r/atom nil)]
    (fn []
      [:div.add-block
       (when @block-menu
         [:div.menu
          {:on-mouse-leave #(reset! block-menu nil)}
          [:ul
           [:li {:on-click #(add-paragraph index block-menu)}
            [:div.icon [:> FontAwesomeIcon {:icon faParagraph}]]
            [:div.label "Paragraph"]]
           [:li {:on-click #(add-heading index block-menu)}
            [:div.icon [:> FontAwesomeIcon {:icon faHeading}]]
            [:div.label "Heading"]]]])
       [:div.add-btn {:on-click #(reset! block-menu true)}
        [:> FontAwesomeIcon {:icon faPlus}]]])))