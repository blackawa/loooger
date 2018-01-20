(ns front.web.util.auth)

(defn user-signed-in? [session]
  (boolean (:id session)))
