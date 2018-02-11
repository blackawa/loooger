(ns auth.proxy.component.proxy-handler
  (:require [integrant.core :as ig])
  (:import [java.net URI]
           [io.undertow.client ClientCallback ClientConnection UndertowClient]
           [io.undertow.server HttpHandler ServerConnection ServerConnection$CloseListener]
           [io.undertow.server.handlers.proxy ProxyHandler ProxyClient ProxyClient$ProxyTarget ProxyConnection]
           [io.undertow.server.handlers.proxy ProxyHandler]
           [io.undertow.util AttachmentKey]
           [org.xnio IoUtils OptionMap ChannelListener]))

(defmethod ig/init-key :auth.proxy.component/proxy-handler [_ {:keys [predicates]}]
  (let [attachment-key (AttachmentKey/create ClientConnection)
        undertow-client (UndertowClient/getInstance)]
    (-> (ProxyHandler/builder)
        (.setProxyClient
         (reify ProxyClient
           (findTarget [this exchange]
             (reify ProxyClient$ProxyTarget))
           (getConnection [this proxy-target exchange proxy-callback timeout time-unit]
             (let [request-uri
                   (.getRequestURI exchange)
                   endpoint
                   (->> predicates
                        (filter #(clojure.string/starts-with? request-uri (:context-path %)))
                        first
                        :endpoint
                        (URI.))
                   connect-notifier
                   (reify ClientCallback
                     (completed [this client-connection]
                       (let [server-connection (.getConnection exchange)]
                         (.putAttachment server-connection attachment-key client-connection)
                         (.addCloseListener server-connection
                                            (reify ServerConnection$CloseListener
                                              (closed [this server-connection]
                                                (IoUtils/safeClose client-connection))))
                         (-> client-connection
                             .getCloseSetter
                             (.set (reify ChannelListener
                                     (handleEvent [this channel]
                                       (-> server-connection (.removeAttachment attachment-key))))))
                         (.completed proxy-callback exchange (ProxyConnection. client-connection ""))))
                     (failed [this io-exception]
                       (.failed proxy-callback exchange)))]
               (.connect undertow-client
                         connect-notifier
                         endpoint
                         (.getIoThread exchange)
                         (-> exchange .getConnection .getByteBufferPool)
                         (OptionMap/EMPTY))))))
        .build)))
