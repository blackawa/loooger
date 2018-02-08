(ns auth.proxy.main
  (:import [io.undertow Undertow]
           [io.undertow.server HttpHandler ServerConnection ServerConnection$CloseListener]
           [io.undertow.util Headers AttachmentKey]
           [io.undertow.client ClientCallback ClientConnection UndertowClient]
           [io.undertow.server.handlers.proxy ProxyHandler ProxyClient ProxyClient$ProxyTarget ProxyConnection]
           [org.xnio IoUtils OptionMap ChannelListener]
           [java.net URI])
  (:gen-class))

(defonce web-server (atom nil))
(defonce api-server (atom nil))
(defn start-web-server []
  (when (not @web-server)
    (-> (Undertow/builder)
        (.addHttpListener 8081 "localhost")
        (.setHandler (reify HttpHandler
                       (handleRequest [this exchange]
                         (-> exchange
                             .getResponseHeaders
                             (.put (Headers/CONTENT_TYPE) "text/html; charset=utf-8"))
                         (-> exchange
                             .getResponseSender
                             (.send "<h1>Hello from web server</h1>")))))
        .build
        (#(reset! web-server %)))
    (.start @web-server)))
(defn stop-web-server []
  (when @web-server
    (.stop @web-server)))
(defn clean-up-web-server []
  (when (not @web-server)
    (stop-web-server))
  (reset! web-server nil))

(defn start-api-server []
  (when (not @api-server)
    (-> (Undertow/builder)
        (.addHttpListener 8082 "localhost")
        (.setHandler (reify HttpHandler
                       (handleRequest [this exchange]
                         (-> exchange
                             .getResponseHeaders
                             (.put (Headers/CONTENT_TYPE) "application/edn; charset=utf-8"))
                         (-> exchange
                             .getResponseSender
                             (.send (str {:a 1 :b 2}))))))
        .build
        (#(reset! api-server %)))
    (.start @api-server)))
(defn stop-api-server []
  (when @api-server
    (.stop @api-server)))
(defn clean-up-api-server []
  (when (not @api-server)
    (stop-api-server))
  (reset! api-server nil))

(defn- connect-notifier [attachment-key uri callback exchange]
  (reify ClientCallback
    (completed [this client-connection]
      (let [server-connection (.getConnection exchange)]
        (.putAttachment server-connection attachment-key client-connection)
        (.addCloseListener server-connection (reify ServerConnection$CloseListener
                                               (closed [this server-connection]
                                                 (IoUtils/safeClose client-connection))))
        (.set (.getCloseSetter client-connection) (reify ChannelListener
                                                    (handleEvent [this channel]
                                                      (.removeAttachment server-connection attachment-key))))
        (.completed callback
                    exchange
                    (ProxyConnection. client-connection (or (.getPath uri) "/")))))
    (failed [this io-exception]
      (.failed callback exchange))))

(defn- uri-proxy-client []
  (let [client (UndertowClient/getInstance)
        attachment-key (AttachmentKey/create ClientConnection)]
    (reify ProxyClient
      (findTarget [this exchange] (reify ProxyClient$ProxyTarget))
      (getConnection [this proxy-target exchange proxy-callback timeout timeunit]
        (let [existing (-> exchange .getConnection (.getAttachment attachment-key))
              uri (.getRequestURI exchange)]
          (if (not (nil? existing))
            (if (.isOpen existing)
              (-> proxy-callback
                  (.completed exchange
                              (ProxyConnection. existing (or (.getPath uri) "/"))))
              (do (-> exchange
                      .getConnection
                      (.removeAttachment attachment-key))
                  (-> client
                      (.connect
                       (connect-notifier attachment-key uri proxy-callback exchange)
                       (URI. (.getScheme uri) nil (.getHost uri) 8081)
                       (.getIoThread exchange)
                       (-> exchange .getConnection .getByteBufferPool)
                       (OptionMap/EMPTY)))))))))))

(defn uri-proxy-handler [proxy-client]
  (-> (ProxyHandler/builder)
      (.setProxyClient proxy-client)
      (.setMaxRequestTime 30000)
      .build))

(defonce proxy-server (atom nil))
(defn start-proxy-server []
  (when (not @proxy-server)
    (-> (Undertow/builder)
        (.addHttpListener 8080 "localhost")
        (.setHandler (uri-proxy-handler (uri-proxy-client)))
        .build
        (#(reset! proxy-server %))))
  (.start @proxy-server))
(defn stop-proxy-server []
  (when @proxy-server
    (.stop @proxy-server)))
(defn clean-up-proxy-server []
  (when (not @proxy-server)
    (stop-proxy-server))
  (reset! proxy-server nil))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
