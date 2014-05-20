(ns irc-journal.handler
  (:use compojure.core
        ring.middleware.edn)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [irc-journal.domain :as d]))

(def app-context
  (if (nil? (System/getProperty "catalina.base"))
    "/irc-journal"
    ""))

(defn response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn register [login password first-name last-name
                email about sex weight born-date]
  (response (d/register
             {:login      login
              :password   password
              :first-name first-name
              :last-name  last-name
              :email      email
              :about      about
              :sex        sex
              :weight     weight
              :born-date  born-date})))

(defroutes app-routes
  (context
   app-context []
   (GET "/" [] (slurp (io/resource "public/html/index.html")))
   (POST "/register/" [login password first-name last-name
                       email about sex weight born-date]
         (register login password first-name last-name
                   email about sex weight born-date))
   (route/resources "/")
   (route/files "/" {:root "public/"})
   (route/not-found "Not Found")))

(def app
  (-> app-routes
      handler/site
      wrap-edn-params))
