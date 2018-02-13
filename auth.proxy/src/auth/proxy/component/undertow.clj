(ns auth.proxy.component.undertow
  (:require [integrant.core :as ig])
  (:import [io.undertow Undertow]))

(defn- run [server]
  (.start server)
  server)

(defmethod ig/init-key :auth.proxy.component/undertow [_ {:keys [port handler]}]
  (println (str "starting undertow server on localhost:" port))
  (-> (Undertow/builder)
      (.addHttpListener port "0.0.0.0")
      (.setHandler handler)
      .build
      run))

(defmethod ig/halt-key! :auth.proxy.component/undertow [_ server]
  (.stop server))
