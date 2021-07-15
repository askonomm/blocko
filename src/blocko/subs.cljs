(ns blocko.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

(reg-sub
 :blocks
 (fn [db _]
   (get db :blocks)))

(reg-sub
 :block-focus
 (fn [db _]
   (get db :block-focus)))

(reg-sub
 :active-block
 (fn [db _]
   (get db :active-block)))