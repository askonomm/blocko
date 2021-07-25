(ns blocko.icons)

(defn trash [color & [clickable?]]
  [:svg
   {:aria-hidden true
    :focusable false
    :width "0.88em"
    :height "1em"
    :preserveAspectRatio "xMidYMid meet"
    :viewBox "0 0 448 512"
    :style (when clickable? {:cursor "pointer"})}
   [:path
    {:d "M432 32H312l-9.4-18.7A24 24 0 0 0 281.1 0H166.8a23.72 23.72 0 0 0-21.4 13.3L136 32H16A16 16 0 0 0 0 48v32a16 16 0 0 0 16 16h416a16 16 0 0 0 16-16V48a16 16 0 0 0-16-16zM53.2 467a48 48 0 0 0 47.9 45h245.8a48 48 0 0 0 47.9-45L416 128H32z"
     :fill color}]])

(defn paragraph [color]
  [:svg
   {:aria-hidden true
    :focusable false
    :width "0.88em"
    :height "1em"
    :preserveAspectRatio "xMidYMid meet"
    :viewBox "0 0 448 512"
    :style
    {:vertical-align "-0.125em"}}
   [:path
    {:d "M448 48v32a16 16 0 0 1-16 16h-48v368a16 16 0 0 1-16 16h-32a16 16 0 0 1-16-16V96h-32v368a16 16 0 0 1-16 16h-32a16 16 0 0 1-16-16V352h-32a160 160 0 0 1 0-320h240a16 16 0 0 1 16 16z"
     :fill color}]])

(defn heading [color]
  [:svg
   {:aria-hidden true
    :focusable false
    :width "1.1em"
    :height "1em"
    :preserveAspectRatio "xMidYMid meet"
    :viewBox "0 0 448 512"
    :style
    {:vertical-align "-0.125em"}}
   [:path
    {:d "M448 96v320h32a16 16 0 0 1 16 16v32a16 16 0 0 1-16 16H320a16 16 0 0 1-16-16v-32a16 16 0 0 1 16-16h32V288H160v128h32a16 16 0 0 1 16 16v32a16 16 0 0 1-16 16H32a16 16 0 0 1-16-16v-32a16 16 0 0 1 16-16h32V96H32a16 16 0 0 1-16-16V48a16 16 0 0 1 16-16h160a16 16 0 0 1 16 16v32a16 16 0 0 1-16 16h-32v128h192V96h-32a16 16 0 0 1-16-16V48a16 16 0 0 1 16-16h160a16 16 0 0 1 16 16v32a16 16 0 0 1-16 16z"
     :fill color}]])

(defn plus [color]
  [:svg
   {:aria-hidden true
    :focusable false
    :width "0.88em"
    :height "1em"
    :preserveAspectRatio "xMidYMid meet"
    :viewBox "0 0 448 512"
    :style
    {:vertical-align "-0.125em"}}
   [:path
    {:d "M416 208H272V64c0-17.67-14.33-32-32-32h-32c-17.67 0-32 14.33-32 32v144H32c-17.67 0-32 14.33-32 32v32c0 17.67 14.33 32 32 32h144v144c0 17.67 14.33 32 32 32h32c17.67 0 32-14.33 32-32V304h144c17.67 0 32-14.33 32-32v-32c0-17.67-14.33-32-32-32z"
     :fill color}]])
