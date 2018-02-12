(ns front.api.util.auth)

(defn session [headers]
  (some-> headers
          (get "x-auth")
          (read-string)))
