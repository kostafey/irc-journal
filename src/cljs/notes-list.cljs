(ns irc-jornal.notes-list
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [irc-jornal.main :as main])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet notes-list
  (str main/app-context "/html/notes-list.html") "#notes-list" [])

(em/defsnippet note-item
  (str main/app-context "/html/notes-list.html") "#note-item"
  [{:keys [note-title note-date note-distance note-time]}]
  "#note-title" (ef/content note-title)
  "#note-date" (ef/content note-date)
  "#note-distance" (ef/content note-distance)
  "#note-time" (ef/content note-time))

(defn ^:export try-load-notes []
  (GET (str main/app-context "/note/list")
       {:handler show-list}))

(defn wrap-note-names [item]
  (into {}
        (map
         (fn [[k v]] [(keyword (str "note-" (name k))) (str v)])
         (seq item))))

(defn show-list [data]
  (main/mark-active nil)
  (ef/at ".container"
         (ef/content (notes-list)))
  (ef/at "#notes-items-list"
         (ef/content
          (map #(note-item (wrap-note-names %)) data))))
