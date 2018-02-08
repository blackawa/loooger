(ns auth.proxy.main
  (:import [io.undertow Undertow]
           [io.undertow.server HttpHandler]
           [io.undertow.util Headers]
           [io.undertow.client ClientCallback ClientConnection]
           [io.undertow.server.handlers.proxy ProxyHandler])
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

;; define ProxyClient
(defn uri-proxy-client [])

(defn connect-notifier [])

;; define ProxyHandler
(defn uri-proxy-handler [proxy-client]
  (-> (ProxyHandler/builder)
      (.setProxyClient proxy-client)
      (.setMaxRequestTime 30000)
      .build))

;; start server
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
