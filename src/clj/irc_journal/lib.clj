(ns irc-journal.lib)

(defn werify [field w-func error msg]
  (if (w-func field) (.append error (str msg "<br>"))))

(defn has-errors? [^StringBuffer error]
  (not (empty? (.toString error))))

(defn trim [val]
  (if (and (not (nil? val))
           (isa? (type val) java.lang.String))
    (.trim val)
    val))

(defn str-to-int [str]
  (if (empty? str)
    0
    (Integer/parseInt str)))

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

