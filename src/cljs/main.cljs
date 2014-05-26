(ns irc-jornal.main
  (:require [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(def app-context "/irc-journal")

(em/defsnippet welcome (str app-context "/html/welcome.html") "#welcome" [])

(defn mark-active [active-item]
  (doseq [item ["#home-li" "#register-li" "#login-li" "#add-note-li"]]
    (ef/at item
           (ef/remove-class "active")))
  (if (not (nil? active-item))
    (ef/at active-item
           (ef/add-class "active"))))

(defn read-form [dom-selector]
  (ef/from dom-selector (ef/read-form-input)))

(defn ^:export show-welcome []
  (mark-active "#home-li")
  (ef/at ".container"
         (ef/do-> (ef/content (welcome)))))

(defn ^:export home []
  (show-welcome))

(defn wrap-data [item]
  (into {}
        (map
         (fn [[k v]] [k (str v)])
         (seq item))))
