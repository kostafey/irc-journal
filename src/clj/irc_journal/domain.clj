(ns irc-journal.domain
  (:use [korma.db]
        [korma.core]
        [clojure.set :only (rename-keys)]))

(defdb irc-journal-db (mysql {:db "irc_journal_db"
                              :user "root"
                              :password "1111"
                              :host "localhost"
                              :port "3306"}))

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

(defn new-date [date-str]
  (.parse (java.text.SimpleDateFormat. "dd.MM.yyyy")
          date-str))

(defentity user
  (table :users)
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

  (select user)
  (exec-raw user-table)
  (insert user (values kostafey))
  )
