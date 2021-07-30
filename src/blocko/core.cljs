(ns blocko.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [blocko.events]
   [blocko.subs]
   [blocko.utils :as utils]
   [blocko.blocks :as blocks]))

(defn- editor [on-change-callback js?]
  (let [blocks (subscribe [:blocks])]
    (r/create-class
     {:component-did-update
      #(when-not (nil? @blocks)
         (if js?
           (on-change-callback (utils/edn->js @blocks))
           (on-change-callback @blocks)))
      :reagent-render
      (fn []
        [blocks/blocks
         {:blocks @blocks}])})))

(defn- create-initial-block! []
  (let [new-block-id (str (random-uuid))]
    (dispatch-sync
     [:add-block
      {:position 0
       :block
       {:id (str (random-uuid))
        :type "paragraph"
        :content ""}}])
    (dispatch-sync
     [:set-focus
      {:id new-block-id
       :where :beginning}])))

(defn- set-blocks! [content js?]
  (dispatch
   [:set-blocks
    (if js?
      (utils/js->edn content)
      content)]))

(defn- set-content! [content js?]
  (if (empty? content)
    (create-initial-block!)
    (set-blocks! content js?)))

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
