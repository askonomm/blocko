(defproject org.clojars.askonomm/blocko "0.2"
  :description "A block-based WYSIWYG editor"
  :url "https://github.com/askonomm/blocko"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.10.866"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [reagent "1.1.0"]
                 [re-frame "1.2.0"]]
  :plugins [[camechis/deploy-uberjar "0.3.0"]]
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :source-paths ["src"]
  :uberjar-name "blocko.jar")