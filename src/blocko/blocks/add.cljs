(ns blocko.blocks.add
  (:require
   ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
   ["@fortawesome/free-solid-svg-icons" :refer (faPlus faParagraph faHeading)]
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.styles :as styles]))

(defn add-paragraph [position block-menu]
  (dispatch
   [:add-block
    {:position position
     :block {:id (str (random-uuid))
             :type "paragraph"
             :content ""}}])
  (reset! block-menu nil))

(defn add-heading [position block-menu]
  (dispatch
   [:add-block
    {:position position
     :block {:id (str (random-uuid))
             :type "heading"
             :content ""}}])
  (reset! block-menu nil))

(defn block-item-paragraph [position block-menu]
  (let [hover? (r/atom nil)]
    (fn []
      [:li
       {:style (if @hover?
                 (styles/style :add-block-menu-list-item-hover)
                 (styles/style :add-block-menu-list-item))
        :on-mouse-enter #(reset! hover? true)
        :on-mouse-leave #(reset! hover? nil)
        :on-click #(add-paragraph position block-menu)}
       [:div.blocko-add-block-menu-list-item-icon
        {:style (styles/style :add-block-menu-list-item-icon)}
        [:> FontAwesomeIcon {:icon faParagraph}]]
       [:div.blocko-add-block-menu-list-item-label
        {:style (styles/style :add-block-menu-list-item-label)}
        "Paragraph"]])))

(defn block-item-heading [position block-menu]
  (let [hover? (r/atom nil)]
    (fn []
      [:li
       {:style (if @hover?
                 (styles/style :add-block-menu-list-item-hover)
                 (styles/style :add-block-menu-list-item))
        :on-mouse-enter #(reset! hover? true)
        :on-mouse-leave #(reset! hover? nil)
        :on-click #(add-heading position block-menu)}
       [:div.blocko-add-block-menu-list-item-icon
        {:style (styles/style :add-block-menu-list-item-icon)}
        [:> FontAwesomeIcon {:icon faHeading}]]
       [:div.blocko-add-block-menu-list-item-label
        {:style (styles/style :add-block-menu-list-item-label)}
        "Heading"]])))

(defn block [{:keys [position]}]
  (let [block-menu (r/atom nil)
        show-indicator? (r/atom nil)]
    (fn []
      [:div.blocko-add-block
       {:style (styles/style :add-block)
        :on-mouse-enter #(reset! show-indicator? true)
        :on-mouse-leave #(reset! show-indicator? nil)}
       (when @show-indicator?
         [:div.blocko-add-block-indicator
          {:style (styles/style :add-block-indicator)}
          [:div.blocko-add-block-btn
           {:style (styles/style :add-block-button)
            :on-click #(reset! block-menu true)}
           [:> FontAwesomeIcon {:icon faPlus}]]])
       (when @block-menu
         [:div.blocko-add-block-menu
          {:style (styles/style :add-block-menu)
           :on-mouse-leave #(reset! block-menu nil)}
          [:ul.blocko-add-block-menu-list
           {:style (styles/style :add-block-menu-list)}
           [block-item-paragraph position block-menu]
           [block-item-heading position block-menu]]])])))