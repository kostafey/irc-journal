(ns irc-jornal.edit-note
  (:require [enfocus.core :as ef]
            [irc-jornal.main :as main])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet note-form
  (str main/app-context "/html/note-form.html") "#note-form" []
  ;; "#register-btn" (ef/set-attr :onclick (str "irc_jornal.register.try_register()"))
  "#note-btn-cancel" (ef/set-attr :onclick (str "irc_jornal.main.show_welcome()"))
  )
