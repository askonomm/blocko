(ns blocko.events
  (:require
   [re-frame.core :refer [reg-event-fx reg-event-db]]
   [blocko.db :as db]
   [blocko.utils :as utils]))

(reg-event-fx
 :initialise-db
 (fn [_ _]
   {:db db/default-db}))

(reg-event-db
 :set-blocks
 (fn [db [_ blocks]]
   (assoc db :blocks blocks)))

(reg-event-db
 :add-block
 (fn [db [_ {:keys [position block]}]]
   (let [blocks (get db :blocks)]
     (assoc db :blocks (utils/block->blocks blocks block position)))))

(reg-event-db
 :set-focus
 (fn [db [_ v]]
   (assoc db :focus v)))

(reg-event-db
 :set-active-block
 (fn [db [_ id]]
   (assoc db :active-block id)))

(reg-event-fx
 :delete-block
 (fn [cofx [_ id]]
   (let [blocks (get-in cofx [:db :blocks])
         updated-blocks (utils/block<-blocks blocks id)]
     (merge
      {:db (assoc (:db cofx) :blocks updated-blocks)}
      (when (empty? updated-blocks)
        (let [new-block-id (str (random-uuid))]
          {:dispatch-n
           [[:add-block {:position 0
                         :block {:id new-block-id
                                 :type "paragraph"}}]
            [:set-focus {:id new-block-id
                         :where :beginning}]]}))))))

(reg-event-fx
 :delete-block-and-focus-on-previous
 (fn [cofx [_ id]]
   (let [blocks (get-in cofx [:db :blocks])
         prev-block (utils/block-before-block id blocks)]
     {:dispatch-n [[:set-focus {:id (get prev-block :id)
                                :where :end}]
                   [:delete-block id]]})))

(reg-event-db
 :update-paragraph-block
 (fn [db [_ {:keys [id content]}]]
   (let [blocks (utils/update-by-predicate #(= (:id %) id) {:content content} (get db :blocks))]
     (assoc db :blocks blocks))))

(reg-event-db
 :update-heading-block
 (fn [db [_ {:keys [id content]}]]
   (let [blocks (utils/update-by-predicate #(= (:id %) id) {:content content} (get db :blocks))]
     (assoc db :blocks blocks))))

