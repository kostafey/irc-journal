(ns irc-journal.domain
  (:use [korma.db]
        [korma.core]
        [clojure.set :only (rename-keys)]))

(defdb irc-journal-db (mysql {:db "irc_journal_db?characterEncoding=utf8"
                              :user "root"
                              :password "1111"
                              :host "localhost"
                              :port "3306"
                              ;; :subname (str "//" host ":" port "/" db)
                              ;; :subname "//localhost:3306/ad_surfing_db?characterEncoding=utf8"
                              }))

(def user-table
  (str "CREATE TABLE users (                                   \n"
       "  id int(11) NOT NULL AUTO_INCREMENT,                  \n"
       "  login varchar(45),                                   \n"
       "  is_admin bit(1) DEFAULT NULL,                        \n"
       "  password varchar(45) NOT NULL,                       \n"
       "  is_active bit(1) DEFAULT NULL,                       \n"
       "  e_mail varchar(45),                                  \n"
       "  first_name varchar(255) CHARACTER SET utf8 NOT NULL, \n"
       "  last_name varchar(45) NOT NULL,                      \n"
       "  register_date datetime DEFAULT NULL,                 \n"
       "  last_activity_date datetime DEFAULT NULL,            \n"
       "  born_date datetime DEFAULT NULL,                     \n"
       "  weight int(11),                                      \n"
       "  sex bit(1) DEFAULT NULL,                             \n"
       "  about longtext CHARACTER SET utf8,                   \n"
       "  PRIMARY KEY (id),                                    \n"
       "  UNIQUE KEY id_UNIQUE (id)                            \n"
       ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  \n"))

(def journal-entry-table
  (str "CREATE TABLE journal_entries (                        \n"
       "  id int(11) NOT NULL AUTO_INCREMENT,                 \n"
       "  user_id int(11) NOT NULL,                           \n"
       "  distance int(11) DEFAULT NULL,                      \n"
       "  time int(11) DEFAULT NULL,                          \n"
       "  speed int(11) DEFAULT NULL,                         \n"
       "  about longtext CHARACTER SET utf8,                  \n"
       "  PRIMARY KEY (id),                                   \n"
       "  UNIQUE KEY id_UNIQUE (id)                           \n"
       ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 \n"))

(defn str-to-int [str]
  (Integer/parseInt str))

(defn new-date [date-str]
  (.parse (java.text.SimpleDateFormat. "dd.MM.yyyy")
          date-str))

(defn new-time [time-str]
  "Time in seconds.
e.g. (new-time \"1:10:12\")"
  (loop [src-seq (map str-to-int (reverse (.split time-str ":")))
         pos 0
         result 0]
    (if (seq src-seq)
      (let [item (first src-seq)]
        (recur (next src-seq)
               (inc pos)
               (+ result
                  (* (get {0 1
                           1 60
                           2 3600
                           3 (* 24 3600)} pos)
                     item))))
      result)))

(defentity journal-entry
  (table :journal_entries)
  (prepare #(rename-keys % {:user-id :user_id}))
  (transform #(rename-keys % {:user_id :user-id})))

(defentity user
  (table :users)
  (has-many journal-entry {:fk :user_id})
  (prepare #(rename-keys % {:is-admin           :is_admin
                            :is-active          :is_active
                            :first-name         :first_name
                            :last-name          :last_name
                            :register-date      :register_date
                            :last-activity-date :last_activity_date
                            :born-date          :born_date}))
  (transform #(rename-keys % {:is_admin            :is-admin
                              :is_active           :is-active
                              :first_name          :first-name
                              :last_name           :last-name
                              :register_date       :register-date
                              :last_activity_date  :last-activity-date
                              :born_date           :born-date})))

(comment
  (def kostafey
    {:login "Kostafey"
     :is-admin true
     :password "secret"
     :is-active true
     :e_mail "kostafey@gmail.com"
     :first-name "Konstantin"
     :last-name "Sedykh"
     :register-date (new-date "25.05.2014")
     :last-activity-date (new-date "25.05.2014")
     :born-date (new-date "25.05.2014")
     :weight 72
     :sex 1
     :about "Have a nice day! ;)"})

  (def first-journal-entity
    {:user_id (-> (select user) first :id)
     :distance 3
     :time (new-time "15:30")
     :about "Восстановительная пробежка"})

  (select user)
  (select journal-entry)

  (first (select user
                 (with journal-entry)))
  ;(exec-raw user-table)
  ;(exec-raw journal-entry-table)
  ;(insert user (values kostafey))
  ;(insert journal-entry (values first-journal-entity))
  )
