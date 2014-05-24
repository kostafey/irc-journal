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

(defn ^:export show-list []
  (main/mark-active nil)
  (ef/at ".container"
         (ef/content (notes-list)))
  (ef/at "#notes-items-list"
         (ef/append (note-item
                     {:note-title "Подготовка к марафону"
                      :note-date "23.05.2014"
                      :note-distance "3"
                      :note-time "15:00"}))))
