(defproject irc-journal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [enfocus "2.1.0-SNAPSHOT"]
                 [cljs-ajax "0.2.3"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler irc-journal.handler/app}
  :profiles {:dev [{:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring-mock "0.1.5"]]}
                   {:plugins [[lein-cljsbuild "1.0.3"]]}]}
  :cljsbuild {
    :builds [{
        :source-paths ["src/cljs"]
        :compiler {
          :output-to "resources/public/js/main.js"
          :optimizations :whitespace
          :pretty-print true}}]})

