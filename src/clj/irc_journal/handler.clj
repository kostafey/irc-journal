(ns irc-journal.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(def app-context
  (if (nil? (System/getProperty "catalina.base"))
    "/irc-journal"
    ""))

(defroutes app-routes
  (context app-context []
           (GET "/" [] (slurp (io/resource "public/html/index.html")))
           (route/resources "/")
           (route/files "/" {:root "public/"})
           (route/not-found "Not Found")))

(def app
  (handler/site app-routes))
