(ns irc-jornal.register
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [jayq.core :as jq]
            [irc-jornal.log-in :as log-in]
            [irc-jornal.main :as main])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet register-form
  (str main/app-context "/html/register-form.html") "#register-form" []
  "#register-btn" (ef/set-attr :onclick (str "irc_jornal.register.try_register()"))
  "#cancel-btn" (ef/set-attr :onclick (str "irc_jornal.main.show_welcome()")))

(defn error-handler [{:keys [status status-text response]}]
  (ef/at "#error"
         (ef/do->
          (ef/add-class "alert alert-dismissable alert-danger")
          (ef/content
           (str
            "<button type=\"button\" class=\"close\" data-dismiss=\"alert\">×</button>"
            response))))
  ;; (.log js/console (str "Something bad happened: " status " " status-text))
  )

(defn ^:export try-register [id]
  (POST (str main/app-context "/register/")
        {:params {:login      (ef/from "#inputLogin" (ef/read-form-input))
                  :password   (ef/from "#inputPassword" (ef/read-form-input))
                  :first-name (ef/from "#inputFirstName" (ef/read-form-input))
                  :last-name  (ef/from "#inputLastName" (ef/read-form-input))
                  :email      (ef/from "#inputEmail" (ef/read-form-input))
                  :about      (ef/from "#textArea" (ef/read-form-input))
                  :sex        (if (= (ef/from "#input-sex" (ef/read-form-input))
                                     "М") "1" "0")
                  :weight     (ef/from "#inputWeight" (ef/read-form-input))
                  :born-date  (ef/from "#input-born-date" (ef/read-form-input))}
         :handler start ; user-registrated
         :error-handler error-handler}))
