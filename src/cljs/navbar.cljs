(ns irc-jornal.navbar
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [jayq.core :as jq]
            [irc-jornal.register :as register]
            [irc-jornal.log-in :as log-in]
            [irc-jornal.edit-note :as edit-note]
            [irc-jornal.main :as main])
  (:use [bootstrap.datepicker :only [datepicker]]
        [jayq.core :only [$ css html]])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet navbar-header (str main/app-context "/html/navbar.html")
  ".navbar" [{:keys [id]}]
  "#register-btn" (ef/set-attr
                   :onclick
                   (str "irc_jornal.navbar.show_register()"))
  "#home-btn" (ef/set-attr
               :onclick
               (str "irc_jornal.main.show_welcome()"))
  "#home-j" (ef/set-attr
             :onclick
             (str "irc_jornal.main.show_welcome()"))
  "#login-btn" (ef/set-attr
                :onclick
                (str "irc_jornal.navbar.show_login()"))
  "#add-note" (ef/set-attr
               :onclick
               (str "irc_jornal.edit_note.show_note_form()"))
  "#notes-list-menu-item" (ef/set-attr
                           :onclick
                           (str "irc_jornal.notes_list.try_load_notes()")))

(defn ^:export show-register []
  (main/mark-active "#register-li")
  (ef/at ".container"
         (ef/content (register/register-form)))
  (let [elem ($ "#dpYears")]
    (.datepicker elem))
  ;; (.run js/Holder)
  )

(defn ^:export show-login []
  (main/mark-active "#login-li")
  (ef/at ".container" (ef/content (log-in/login))))

