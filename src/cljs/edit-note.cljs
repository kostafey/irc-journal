(ns irc-jornal.edit-note
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [irc-jornal.main :as main])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet note-form
  (str main/app-context "/html/note-form.html") "#note-form" []
  "#note-btn-edit" (ef/set-attr :onclick (str "irc_jornal.edit_note.try_update_note()"))
  "#note-btn-cancel" (ef/set-attr :onclick (str "irc_jornal.main.show_welcome()")))

(defn ^:export try-update-note [id]
  (POST (str main/app-context "/update-note/")
        {:params {:distance (ef/from "#input-distance" (ef/read-form-input))
                  :time     (ef/from "#input-time" (ef/read-form-input))
                  :about    (ef/from "#input-about" (ef/read-form-input))}
         :handler main/start ; user-registrated
         :error-handler error-handler}))

