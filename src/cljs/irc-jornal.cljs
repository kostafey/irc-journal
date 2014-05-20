(ns irc-jornal.core
  (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]]
            [jayq.core :as jq]
            )
  (:use [bootstrap.datepicker :only [datepicker]]
        [jayq.core :only [$ css html]])
  (:require-macros [enfocus.macros :as em]))

(def app-context "/irc-journal")

(em/defsnippet navbar-header (str app-context "/html/navbar.html") ".navbar" [{:keys [id]}]
  "#register-btn" (ef/set-attr
                   :onclick
                   (str "irc_jornal.core.show_register()"))
  "#home-btn" (ef/set-attr
               :onclick
               (str "irc_jornal.core.show_welcome()"))
  "#home-j" (ef/set-attr
             :onclick
             (str "irc_jornal.core.show_welcome()"))
  "#login-btn" (ef/set-attr
                :onclick
                (str "irc_jornal.core.show_login()")))

(em/defsnippet register-form
  (str app-context "/html/register-form.html") "#register-form" []
  "#register-btn" (ef/set-attr :onclick (str "irc_jornal.core.try_register()")))

(em/defsnippet welcome (str app-context "/html/welcome.html") "#welcome" [])
(em/defsnippet login (str app-context "/html/login.html") "#login" [])

(defn mark-active [active-item]
  (doseq [item ["#home-li" "#register-li" "#login-li"]]
    (ef/at item
           (ef/remove-class "active")))
  (ef/at active-item
         (ef/add-class "active")))

(defn ^:export show-register []
  (mark-active "#register-li")
  (ef/at ".container"
         (ef/do-> (ef/content (register-form))))
  (let [elem ($ "#dpYears")]
    (.datepicker elem)))

(defn ^:export show-welcome []
  (mark-active "#home-li")
  (ef/at ".container"
         (ef/do-> (ef/content (welcome)))))

(defn ^:export show-login []
  (mark-active "#login-li")
  (ef/at ".container" (ef/content (login))))

;;(if (ef/from "#optionsRadios1" (ef/read-form-input)) 1 0)

(defn ^:export try-register [id]
  (POST "/irc-journal/register/"
        {:params {:login      (ef/from "#inputLogin" (ef/read-form-input))
                  :password   (ef/from "#inputPassword" (ef/read-form-input))
                  :first-name (ef/from "#inputFirstName" (ef/read-form-input))
                  :last-name  (ef/from "#inputLastName" (ef/read-form-input))
                  :email      (ef/from "#inputEmail" (ef/read-form-input))
                  :about      (ef/from "#textArea" (ef/read-form-input))
                  :sex        1
                  :weight     (ef/from "#inputWeight" (ef/read-form-input))
                  :born-date  (ef/from "#dpYears" (ef/read-form-input))}
         ;; :handler article-saved
         ;; :error-handler error-handler
         })
  )

(defn start []
  (ef/at "#header"
         (ef/do-> (ef/content (navbar-header))
                  ;; (ef/append (blog-sidebar))
                  ))
  (ef/at ".container"
         (ef/append (welcome)))
  ;; (let [elem ($ "#dpYears")]
  ;;   (.datepicker elem))
  ;; (try-load-articles)
  )

(set! (.-onload js/window) #(em/wait-for-load (start)))

;; (em/defsnippet blog-sidebar "/html/blog.html" "#sidebar" [])
;; (em/defsnippet blog-content "/html/blog.html" "#content" [])
;; (em/defsnippet blog-create-post "/html/blog.html" "#article-form" [])

;; (em/defsnippet blog-edit-post "/html/blog.html" "#article-form"
;;   [{:keys [id title body]}]
;;   "#article-title" (ef/set-attr :value title)
;;   "#article-body" (ef/content body)
;;   "#save-btn" (ef/set-attr :onclick (str "client.core.try_update_article(" id ")")))

;; (em/defsnippet blog-post-view "/html/blog.html" "#view-article" [{:keys [title body]}]
;;   "#view-title" (ef/content title)
;;   "#view-body" (ef/content body))

;; (em/defsnippet blog-post "/html/blog.html"
;;   ".blog-post"  [{:keys [id title body]}]
;;   "#blog-post-title" (ef/content title)
;;   "#blog-post-body" (ef/content body)
;;   "#article-view" (ef/set-attr :onclick
;;                     (str "client.core.try_view_article(" id ")"))
;;   "#article-edit" (ef/set-attr :onclick
;;                     (str "client.core.try_edit_article(" id ")"))
;;   "#article-delete" (ef/set-attr :onclick
;;   (str "if(confirm('Really delete?')) client.core.try_delete_article(" id ")")))

;; (defn article-list [data]
;;   (ef/at "#inner-content" (ef/content (map blog-post data))))

;; (defn try-load-articles []
;;   (GET "/article/list"
;;        {:handler article-list}))

;; (defn start []
;;   (ef/at ".container"
;;          (ef/do-> (ef/content (blog-header))
;;                   (ef/append (blog-content))
;;                   (ef/append (blog-sidebar))))
;;   (try-load-articles))

;; (defn hide-new-post-btn []
;;   (ef/at "#new-post" (ef/set-attr :style "display:none;")))

;; (defn ^:export show-create []
;;   (ef/at "#inner-content" (ef/content (blog-create-post)))
;;   (hide-new-post-btn))

;; (defn ^:export close-form []
;;   (start))

;; (defn error-handler [{:keys [status status-text]}]
;;   (.log js/console (str "Something bad happened: " status " " status-text)))

;; (defn article-saved [response]
;;   (close-form))

;; (defn ^:export try-update-article [id]
;;   (POST (str "/article/update/" id)
;;         {:params {:title (ef/from "#article-title" (ef/read-form-input))
;;                   :body  (ef/from "#article-body" (ef/read-form-input))}
;;          :handler article-saved
;;          :error-handler error-handler}))

;; (defn ^:export article-edit [data]
;;   (ef/at "#inner-content" (ef/content (blog-edit-post data)))
;;   (hide-new-post-btn))

;; (defn ^:export try-edit-article [id]
;;   (GET (str "/article/" id)
;;         {:handler article-edit
;;          :error-handler error-handler}))

;; (defn article-view [data]
;;   (hide-new-post-btn)
;;   (ef/at "#inner-content" (ef/content (blog-post-view data))))

;; (defn ^:export try-view-article [id]
;;   (GET (str "/article/" id)
;;         {:handler article-view
;;          :error-handler error-handler}))

;; (defn ^:export try-delete-article [id]
;;   (POST (str "/article/delete/" id)
;;         {:handler article-saved
;;          :error-handler error-handler}))

;; (defn ^:export try-create-article []
;;   (.log js/console (ef/from "#article-title" (ef/read-form-input)))
;;   (.log js/console (ef/from "#article-body" (ef/read-form-input)))
;;   (POST "/article/create"
;;         {:params {:title (ef/from "#article-title" (ef/read-form-input))
;;                   :body (ef/from "#article-body" (ef/read-form-input))}
;;          :handler article-saved
;;          :error-handler error-handler}))
