(ns auth.proxy.component.session-store
  (:require [integrant.core :as ig]
            [taoensso.carmine :as car]))

(defmethod ig/init-key :auth.proxy.component/session-store [_ {:keys [host port]}]
  (let [conn {:pool {} :spec {:uri (str "redis://" host ":" port)}}]
    (car/wcar conn (car/ping)) ;; check connection
    conn))
