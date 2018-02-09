(ns auth.proxy.component.undertow
  (:require [integrant.core :as ig])
  (:import [io.undertow Undertow]
           [io.undertow.server HttpHandler ServerConnection ServerConnection$CloseListener]
           [io.undertow.util Headers AttachmentKey]
           [io.undertow.client ClientCallback ClientConnection UndertowClient]
           [io.undertow.server.handlers.proxy ProxyHandler ProxyClient ProxyClient$ProxyTarget ProxyConnection]
           [org.xnio IoUtils OptionMap ChannelListener]
           [java.net URI]))

(defn- run [server]
  (.start server)
  server)

(defn- proxy-client []
  (reify HttpHandler
    (handleRequest [this exchange]
      (-> exchange
          .getResponseHeaders
          (.put (Headers/CONTENT_TYPE) "text/html; charset=utf-8"))
      (-> exchange
          .getResponseSender
          (.send "<h1>Hello from web server</h1>")))))

(defmethod ig/init-key :auth.proxy.component/undertow [_ {:keys [port]}]
  (println (str "start undertow server on localhost:" port))
  (-> (Undertow/builder)
      (.addHttpListener port "localhost")
      (.setHandler (proxy-client))
      .build
      run))

(defmethod ig/halt-key! :auth.proxy.component/undertow [_ server]
  (.stop server))
