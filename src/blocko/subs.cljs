(ns blocko.subs
  (:require
   [re-frame.core :refer [reg-sub]]))

(reg-sub
 :blocks
 (fn [db _]
   (get db :blocks)))

(reg-sub
 :focus
 (fn [db _]
   (get db :focus)))

(reg-sub
 :active-block
 (fn [db _]
   (get db :active-block)))