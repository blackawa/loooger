(ns front.web.component.session-store
  (:require [taoensso.carmine.ring :refer [carmine-store]]
            [integrant.core :as ig]))

(defmethod ig/init-key :front.web.component/session-store [_ {:keys [host port]}]
  (carmine-store {:pool {} :spec {:uri (str "redis://" (or host "127.0.0.1") ":" (or port "6379"))}}))
