(ns irc-jornal.core
  (:require [enfocus.core :as ef]
            [irc-jornal.main :as main]
            [irc-jornal.navbar :as navbar]
            [clojure.browser.repl :as repl])
  (:require-macros [enfocus.macros :as em]))

(repl/connect "http://localhost:9000/repl")

(defn ^:export start []
  (ef/at "#header"
         (ef/do-> (ef/content (navbar/navbar-header))
                  ;; (ef/append (blog-sidebar))
                  ))
  (ef/at ".container"
         (ef/content (main/welcome))))

(set! (.-onload js/window) #(em/wait-for-load (start)))

