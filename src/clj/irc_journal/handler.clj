(ns irc-journal.handler
  (:use compojure.core
        ring.middleware.edn
        [irc-journal.lib])
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
   :headers {"Content-Type" "application/edn; charset=utf-8"}
   :body (pr-str data)})

(defn register [login password first-name last-name
                email about sex weight born-date]
  (let [[login password first-name last-name
         email about sex weight born-date]
        (map trim [login password first-name last-name
                   email about sex weight born-date])
        error (StringBuffer.)]
    (werify login empty? error "Поле 'Логин' не может быть пустым")
    (werify password empty? error "Поле 'Пароль' не может быть пустым")
    (werify first-name empty? error "Поле 'Имя' не может быть пустым")
    (werify last-name empty? error "Поле 'Фамилия' не может быть пустым")
    (werify email empty? error "Поле 'Email' не может быть пустым")
    (werify born-date empty? error "Поле 'Дата рождения' не может быть пустым")
    (if (has-errors? error)
      (response (.toString error) 400)
      (response (d/register
                 {:login      login
                  :password   password
                  :first-name first-name
                  :last-name  last-name
                  :email      email
                  :about      about
                  :sex        (.equals "1" sex)
                  :weight     (str-to-int weight)
                  :born-date  (new-date born-date)})))))

(defn login-fn [login password]
  (let [user-id (d/login login password)]
    (if (nil? user-id)
      (response "Нет пользователя с такими логином/паролем." 400)
      (response user-id 200))))

(defn update-note [distance time about]
  (let [note {:user-id  1
              :distance (str-to-int distance)
              :time     (new-time time)
              :about    about}]
    (response (d/update-note note))))

(defroutes app-routes
  (context
   app-context []
   (GET "/" [] (slurp (io/resource "public/html/index.html")))
   (POST "/register/" [login password first-name last-name
                       email about sex weight born-date]
         (register login password first-name last-name
                   email about sex weight born-date))
   (POST "/login/" [login password] (login-fn login password))
   (POST "/update-note/" [distance time about]
         (update-note distance time about))
   (route/resources "/")
   (route/files "/" {:root "public/"})
   (route/not-found "Not Found")))

(def app
  (-> app-routes
      handler/site
      wrap-edn-params))
