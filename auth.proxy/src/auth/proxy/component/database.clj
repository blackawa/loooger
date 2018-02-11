(ns auth.proxy.component.database
  (:require [hikari-cp.core :as hikari-cp]
            [integrant.core :as ig]))

(defmethod ig/init-key :auth.proxy.component/database [_ {:keys [config]}]
  (hikari-cp/make-datasource config))

(defmethod ig/halt-key! :auth.proxy.component/database [_ datasource]
  (hikari-cp/close-datasource datasource))
