(defproject irc-journal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [enfocus "2.1.0-SNAPSHOT"]
                 [jayq "2.5.1"]
                 [cljs-ajax "0.2.3"]
                 [korma "0.3.1"]
                 [mysql-java "5.1.21"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler irc-journal.handler/app}
  :profiles {:dev [{:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring-mock "0.1.5"]]}
                   {:plugins [[lein-cljsbuild "1.0.3"]]}]}
  :source-paths ["src/clj"]
  :cljsbuild {
    :builds [{
        :source-paths ["src/cljs"]
        :compiler {
          :foreign-libs [{:file "resources/public/js/bootstrap-datepicker.js"
                          :provides ["bootstrap.datepicker"]}]
          :output-to "resources/public/js/main.js"
          :optimizations :whitespace
          :pretty-print true}}]})

