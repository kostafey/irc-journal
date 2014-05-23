(ns irc-jornal.log-in
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [irc-jornal.main :as main])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet login
  (str main/app-context "/html/login.html") "#login" []
  "#l-btn-login" (ef/set-attr :onclick (str "irc_jornal.log_in.try_login()"))
  "#l-btn-cancel" (ef/set-attr :onclick (str "irc_jornal.core.start()")))

(defn ^:export try-login [id]
  (POST (str main/app-context "/login/")
        {:params {:login      (ef/from "#inputLogin" (ef/read-form-input))
                  :password   (ef/from "#inputPassword" (ef/read-form-input))}
         :handler start ; user-registrated
         :error-handler error-handler}))
