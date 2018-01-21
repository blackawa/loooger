(ns front.api.component.redis
  (:require [integrant.core :as ig]))

(defrecord Boundary [spec])

(defmethod ig/init-key :front.api.component/redis [_ {:keys [host port]}]
  (-> {:pool {} :spec {:uri (str "redis://" (or host "127.0.0.1") ":" (or port "6379"))}}
      (->Boundary)))
