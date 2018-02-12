(ns front.web.util.auth)

(defn session [headers]
  (println headers)
  (-> (get headers "x-auth")
      (#(when % (read-string %)))))
