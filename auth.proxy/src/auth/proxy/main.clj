(ns auth.proxy.main
  (:import [io.undertow Undertow]
           [io.undertow.server HttpHandler]
           [io.undertow.util Headers])
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
  (.stop @web-server))

(defn clean-up-web-server []
  (reset! web-server nil))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
