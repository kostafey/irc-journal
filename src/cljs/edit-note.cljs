(ns irc-jornal.edit-note
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [irc-jornal.main :as main]
            [irc-jornal.notes-list :refer [try-load-notes]])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet note-form
  (str main/app-context "/html/note-form.html") "#note-form"
  [{:keys [id title date distance time about]}]
  "#note-btn-edit" (ef/set-attr :onclick (str "irc_jornal.edit_note.try_update_note()"))
  "#note-btn-cancel" (ef/set-attr :onclick (str "irc_jornal.notes_list.try_load_notes()"))
  "#note-id" (ef/set-form-input id)
  "#note-title" (ef/set-form-input title)
  "#note-distance" (ef/set-form-input distance)
  "#note-time" (ef/set-form-input time)
  "#note-date" (ef/set-form-input date)
  "#note-about" (ef/set-form-input about))

(defn ^:export try-update-note [id]
  (POST (str main/app-context "/update-note/")
        {:params {:distance (main/read-form "#note-distance")
                  :time     (main/read-form "#note-time")
                  :about    (main/read-form "#note-about")
                  :title    (main/read-form "#note-title")}
         :handler try-load-notes
         :error-handler error-handler}))

(defn ^:export try-load-note [id]
  (GET (str main/app-context "/note/load/")
       {:params {:id id}
        :handler load-note-form}))

(defn ^:export show-note-form []
  (main/mark-active "#add-note-li")
  (ef/at ".container" (ef/content (note-form {}))))

(defn ^:export load-note-form [data]
  (ef/at ".container" (ef/content (note-form (main/wrap-data data)))))
