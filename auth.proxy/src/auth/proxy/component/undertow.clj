(ns auth.proxy.component.undertow
  (:require [integrant.core :as ig])
  (:import [io.undertow Undertow]
           [io.undertow.server HttpHandler]
           [io.undertow.server.handlers.proxy ProxyHandler]
           [io.undertow.util Headers]))

(defn- run [server]
  (.start server)
  server)

(defmethod ig/init-key :auth.proxy.component/undertow [_ {:keys [port proxy-client]}]
  (println proxy-client)
  (println (str "start undertow server on localhost:" port))
  (-> (Undertow/builder)
      (.addHttpListener port "localhost")
      (.setHandler (-> (ProxyHandler/builder)
                       (.setProxyClient proxy-client)
                       .build))
      .build
      run))

(defmethod ig/halt-key! :auth.proxy.component/undertow [_ server]
  (.stop server))
