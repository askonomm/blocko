(ns blocko.blocks.add
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]
   [blocko.styles :as styles]
   [blocko.icons :as icons]))

(defn- add-paragraph!
  "Dispatches an event to create a paragraph block at a given 
  `position`, after which it will close the block menu and hide
  the indicator."
  [position block-menu show-indicator?]
  (dispatch
   [:add-block
    {:position position
     :block {:id (str (random-uuid))
             :type "paragraph"
             :content ""}}])
  (reset! block-menu nil)
  (js/setTimeout #(reset! show-indicator? nil) 50))

(defn- add-heading!
  "Dispatches an event to create a heading block at a given 
  `position`, after which it will close the block menu and hide
  the indicator."
  [position block-menu show-indicator?]
  (dispatch
   [:add-block
    {:position position
     :block {:id (str (random-uuid))
             :type "heading"
             :content ""}}])
  (reset! block-menu nil)
  (js/setTimeout #(reset! show-indicator? nil) 50))

(def blocks
  [{:name "Paragraph"
    :icon icons/paragraph
    :on-click add-paragraph!}
   {:name "Heading"
    :icon icons/heading
    :on-click add-heading!}])

(defn- item
  "Renders a given block item in the block menu."
  [{:keys [name icon on-click]} position block-menu show-indicator?]
  (let [hover? (r/atom nil)]
    (fn []
      [:li
       {:style (if @hover?
                 (styles/style :add-block-menu-list-item-hover)
                 (styles/style :add-block-menu-list-item))
        :on-mouse-enter #(reset! hover? true)
        :on-mouse-leave #(reset! hover? nil)
        :on-click #(on-click position block-menu show-indicator?)}
       [:div.blocko-add-block-menu-list-item-icon
        {:style (styles/style :add-block-menu-list-item-icon)}
        [icon (styles/style :add-block-menu-list-item-icon-color)]]
       [:div.blocko-add-block-menu-list-item-label
        {:style (styles/style :add-block-menu-list-item-label)}
        name]])))

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
           [icons/plus (styles/style :add-block-button-icon-color)]]])
       (when @block-menu
         [:div.blocko-add-block-menu
          {:style (styles/style :add-block-menu)
           :on-mouse-leave #(reset! block-menu nil)}
          [:ul.blocko-add-block-menu-list
           {:style (styles/style :add-block-menu-list)}
           (map
            (fn [i]
              ^{:key (get i :name)}
              [item i position block-menu show-indicator?])
            blocks)]])])))