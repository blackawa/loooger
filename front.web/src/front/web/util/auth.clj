(ns front.web.util.auth)

(defn session [headers]
  (-> (get headers "x-auth")
      (#(when % (read-string %)))))
