(ns blocko.styles.dark)

(def container
  {:width "600px"
   :font-family "sans-serif"
   :font-size "15px"
   :box-sizing "border-box"
   :position "relative"
   :padding 0
   :margin 0})

(def controls
  {:display "flex"
   :align-items "center"
   :width "40px"
   :height "calc(100% - 2px)"
   :box-sizing "border-box"
   :position "absolute"
   :left "-40px"})

(def control
  {:cursor "pointer"
   :opacity "0.5"
   :box-sizing "border-box"
   :position "relative"})

(def block
  {:color "#fbfbfb"
   :box-sizing "border-box"
   :position "relative"})

(def add-block
  {:display "flex"
   :width "100%"
   :height "4px"
   :align-items "center"
   :box-sizing "border-box"
   :position "relative"
   :padding-top "14px"
   :padding-bottom "14px"})

(def add-block-indicator
  {:display "block"
   :width "100%"
   :height "2px"
   :background "#272727"
   :box-sizing "border-box"
   :position "relative"})

(def add-block-button
  {:display "block"
   :width "30px"
   :height "30px"
   :line-height "27px"
   :text-align "center"
   :color "#111"
   :text-shadow "0 1px 0 #fff"
   :background "linear-gradient(#fff, #eee)"
   :border "1px solid #fff"
   :border-radius "100%"
   :cursor "pointer"
   :box-sizing "border-box"
   :z-index "10"
   :position "absolute"
   :top "calc(50% - 14px)"
   :left "calc(50% - 15px)"})

(def add-block-menu
  {:display "block"
   :width "400px"
   :height "auto"
   :background "#111"
   :box-shadow "0px 3px 20px rgba(0,0,0,.5), 0px 0px 50px rgba(0,0,0,.5)"
   :border "1px solid #272727"
   :border-radius "5px"
   :box-sizing "border-box"
   :z-index 20
   :position "absolute"
   :padding 25
   :top -50
   :left "calc(50% - 200px)"})

(def add-block-menu-list
  {:display "grid"
   :grid-template-columns "1fr 1fr 1fr"
   :grid-gap 20
   :box-sizing "border-box"
   :position "relative"
   :padding 0
   :margin 0})

(def add-block-menu-list-item
  {:display "flex"
   :flex-direction "column"
   :align-items "center"
   :justify-content "center"
   :height 75
   :list-style "none"
   :text-align "center"
   :cursor "pointer"
   :border "1px solid #272727"
   :border-radius "5px"
   :box-sizing "border-box"
   :opacity ".75"
   :transition "all 0.1s linear"
   :position "relative"})

(def add-block-menu-list-item-hover
  {:display "flex"
   :flex-direction "column"
   :align-items "center"
   :justify-content "center"
   :height 75
   :list-style "none"
   :text-align "center"
   :cursor "pointer"
   :background "linear-gradient(#171717, #111)"
   :border "1px solid #272727"
   :border-radius "5px"
   :box-sizing "border-box"
   :opacity "1"
   :transition "all 0.1s linear"
   :position "relative"})

(def add-block-menu-list-item-icon
  {:font-size 23
   :color "#fff"
   :line-height "1"
   :box-sizing "border-box"
   :position "relative"})

(def add-block-menu-list-item-label
  {:font-size 10
   :font-weight "400"
   :text-transform "uppercase"
   :color "#eee"
   :line-height "1"
   :letter-spacing 0.5
   :box-sizing "border-box"
   :position "relative"
   :margin-top 10})

(def paragraph-block-content
  {:outline "none"
   :position "relative"
   :box-sizing "border-box"
   :padding 0
   :margin 0})

(def paragraph-block-content-empty
  {:outline "none"
   :color "#666"
   :position "absolute"
   :box-sizing "border-box"
   :padding 0
   :margin 0})

(def heading-block-content
  {:display "block"
   :width "100%"
   :overflow "hidden"
   :font-family "sans-serif"
   :font-size 24
   :line-height "1"
   :font-weight "800"
   :letter-spacing -2
   :color "#fff"
   :background "transparent"
   :outline "none"
   :resize "none"
   :border 0
   :position "relative"
   :box-sizing "border-box"
   :padding 0
   :margin 0})

(def theme
  {:container container
   :controls controls
   :control control
   :block block
   :add-block add-block
   :add-block-indicator add-block-indicator
   :add-block-button add-block-button
   :add-block-menu add-block-menu
   :add-block-menu-list add-block-menu-list
   :add-block-menu-list-item add-block-menu-list-item
   :add-block-menu-list-item-hover add-block-menu-list-item-hover
   :add-block-menu-list-item-icon add-block-menu-list-item-icon
   :add-block-menu-list-item-label add-block-menu-list-item-label
   :paragraph-block-content paragraph-block-content
   :paragraph-block-content-empty paragraph-block-content-empty
   :heading-block-content heading-block-content})