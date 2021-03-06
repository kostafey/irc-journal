(ns irc-journal.domain
  (:use [korma.db]
        [korma.core]
        [clojure.set :only (rename-keys)]
        [irc-journal.lib]))

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
       "  img_path varchar(45),                                \n"
       "  is_admin bit(1) DEFAULT NULL,                        \n"
       "  password varchar(45) NOT NULL,                       \n"
       "  is_active bit(1) DEFAULT NULL,                       \n"
       "  email varchar(45),                                   \n"
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

(def geo-table
  (str "CREATE TABLE IF NOT EXISTS geo (                      \n"
       "  id int(11) NOT NULL AUTO_INCREMENT,                 \n"
       "  place_name varchar(255) NOT NULL,                   \n"
       "  coordinates point NOT NULL,                         \n"
       "  PRIMARY KEY (`id`)                                  \n"
       ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 \n"))

(def note-table
  (str "CREATE TABLE notes (                                  \n"
       "  id int(11) NOT NULL AUTO_INCREMENT,                 \n"
       "  user_id int(11) NOT NULL,                           \n"
       "  geo_id int(11) DEFAULT NULL,                        \n"
       "  title varchar(255) DEFAULT NULL,                    \n"
       "  event_date datetime DEFAULT NULL,                   \n"
       "  distance int(11) DEFAULT NULL,                      \n"
       "  time int(11) DEFAULT NULL,                          \n"
       "  speed int(11) DEFAULT NULL,                         \n"
       "  about longtext CHARACTER SET utf8,                  \n"
       "  PRIMARY KEY (id),                                   \n"
       "  UNIQUE KEY id_UNIQUE (id)                           \n"
       ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 \n"))

(defentity geo-entry
  (table :geo)
  (prepare #(rename-keys % {:place-name :place_name}))
  (transform #(rename-keys % {:place_name :place-name})))

(defentity note-entry
  (table :notes)
  (has-many geo-entry {:fk :geo_id})
  (prepare #(rename-keys % {:user-id :user_id
                            :gei-id :gei_id
                            :event-date :event_date}))
  (transform #(rename-keys % {:user_id :user-id
                              :gei_id :gei-id
                              :event_date :event-date})))

(defentity user
  (table :users)
  (has-many note-entry {:fk :user_id})
  (prepare #(rename-keys % {:img-path           :img_path
                            :is-admin           :is_admin
                            :is-active          :is_active
                            :first-name         :first_name
                            :last-name          :last_name
                            :register-date      :register_date
                            :last-activity-date :last_activity_date
                            :born-date          :born_date}))
  (transform #(rename-keys % {:img_path            :img-path
                              :is_admin            :is-admin
                              :is_active           :is-active
                              :first_name          :first-name
                              :last_name           :last-name
                              :register_date       :register-date
                              :last_activity_date  :last-activity-date
                              :born_date           :born-date})))

(defn register [new-user]
  (insert user (values new-user)))

(defn login [login password]
  (let [found-user (first
                    (select user
                            (fields :id :password)
                            (where {:login login})))]
    (if (.equals (:password found-user) password)
      (:id found-user)
      nil)))

(defn update-note [note]
  (insert note-entry (values note)))

(defn notes-list []
  (select note-entry))

(defn get-note [id]
  (first (select note-entry
                 (where {:id id}))))

(comment
  (def kostafey
    {:login "Kostafey"
     :is-admin true
     :password "secret"
     :is-active true
     :email "kostafey@gmail.com"
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
  (select note-entry)

  (first (select user
                 (with journal-entry)))
  ;(exec-raw user-table)
  ;(exec-raw note-table)
  ;(exec-raw geo-table)
  ;(insert user (values kostafey))
  ;(insert journal-entry (values first-journal-entity))
  )
