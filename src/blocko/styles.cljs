(ns blocko.styles
  (:require
   [blocko.styles.dark :as dark]
   [blocko.styles.light :as light]))

(defn style [s]
  (let [theme (if (or (undefined? (.-blockoOptions js/window))
                      (undefined? (.-theme (.-blockoOptions js/window))))
                "dark"
                (.-theme (.-blockoOptions js/window)))]
    (cond (= "light" theme)
          (get light/theme s)
          :else
          (get dark/theme s))))