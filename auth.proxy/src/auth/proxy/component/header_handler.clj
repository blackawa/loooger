(ns auth.proxy.component.header-handler
  (:require [integrant.core :as ig])
  (:import [io.undertow.server HttpHandler]
           [io.undertow.util HttpString]))

(defmethod ig/init-key :auth.proxy.component/header-handler [_ {:keys [next-handler]}]
  (reify HttpHandler
    (handleRequest [this exchange]
      (-> exchange .getRequestHeaders (.put (HttpString. "X-Authorization") (str {:a 1 :b 2})))
      (.handleRequest next-handler exchange))))
