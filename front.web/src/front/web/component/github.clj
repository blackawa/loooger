(ns front.web.component.github
  (:require [integrant.core :as ig]))

(defrecord Boundary [client-id secret])

(defmethod ig/init-key :front.web.component/github [_ {:keys [secrets]}]
  (->Boundary (get-in secrets [:github :client-id])
              (get-in secrets [:github :client-secret])))
