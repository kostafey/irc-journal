(ns irc-journal.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defroutes app-routes
  ;; (context "/irc-journal" request
           (GET "/" [] (slurp (io/resource "public/html/index.html")))
           (route/resources "/")
           (route/files "/" {:root "public/"})
           (route/not-found "Not Found"))
;; )

(def app
  (handler/site app-routes))
