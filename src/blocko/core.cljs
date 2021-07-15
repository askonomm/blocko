(ns blocko.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [blocko.events]
   [blocko.subs]
   [blocko.utils :as utils]
   [blocko.blocks :as blocks]))

(defn editor [on-change-callback]
  (let [blocks-sub (subscribe [:blocks])
        block-focus (subscribe [:block-focus])]
    (fn []
      (on-change-callback (utils/edn->js @blocks-sub))
      [blocks/blocks
       {:blocks @blocks-sub
        :block-focus @block-focus}])))

(defn set-content! [content]
  (if (empty? content)
    (dispatch [:add-block {:position 0
                           :block
                           {:id (str (random-uuid))
                            :type "paragraph"
                            :content ""}}])
    (dispatch [:set-blocks (utils/js->edn content)])))

(defn ^:export init [args]
  (let [{:keys [container initialContent onChange]} (js->clj args :keywordize-keys true)]
    (dispatch-sync [:initialise-db])
    (set-content! initialContent)
    (rdom/render [editor onChange] (.querySelector js/document container))))