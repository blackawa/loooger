(ns front.web.component.session-store
  (:require [integrant.core :as ig]
            [taoensso.carmine.ring :refer [carmine-store]]))

(defmethod ig/init-key :front.web.component/session-store [_ {:keys [host port]}]
  (carmine-store {:pool {} :spec {:uri (str "redis://" (or host "127.0.0.1") ":" (or port "6379"))}}))
