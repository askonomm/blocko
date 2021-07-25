(ns blocko.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [blocko.events]
   [blocko.subs]
   [blocko.utils :as utils]
   [blocko.blocks :as blocks]))

(defn editor [on-change-callback js?]
  (let [blocks-sub (subscribe [:blocks])
        block-focus (subscribe [:block-focus])]
    (fn []
      (when-not (nil? @blocks-sub)
        (if js?
          (on-change-callback (utils/edn->js @blocks-sub))
          (on-change-callback @blocks-sub)))
      [blocks/blocks
       {:blocks @blocks-sub
        :block-focus @block-focus}])))

(defn set-content! [content js?]
  (if (empty? content)
    (dispatch [:add-block
               {:position 0
                :block
                {:id (str (random-uuid))
                 :type "paragraph"
                 :content ""}}])
    (dispatch [:set-blocks
               (if js?
                 (utils/js->edn content)
                 content)])))

(defn run [args]
  (let [{:keys [content options on-change js?]} args]
    (r/create-class
     {:component-did-mount
      (fn []
        (dispatch-sync [:initialise-db])
        (set-content! content js?)
        (when options
          (set! (.-blockoOptions js/window) (clj->js options))))
      :reagent-render
      #(editor on-change js?)})))

(defn ^:export init [args]
  (let [{:keys [container content options onChange]} (js->clj args :keywordize-keys true)]
    (rdom/render [run {:content content
                       :options options
                       :on-change onChange
                       :js? true}] (.querySelector js/document container))))
