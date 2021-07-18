(defproject org.clojars.askonomm/blocko "0.1"
  :description "A block-based WYSIWYG editor"
  :url "https://github.com/askonomm/blocko"
  :min-lein-version "2.0.0"
  :dependencies [[reagent "1.1.0"]
                 [re-frame "1.2.0"]]
  :plugins [[camechis/deploy-uberjar "0.3.0"]]
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :repositories [["releases" {:url "https://clojars.org"}]]
  :uberjar-name "blocko.jar")