(ns auth.proxy.component.proxy-client
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :auth.proxy.component/proxy-client [_ {:keys [predicates]}]
  predicates)
