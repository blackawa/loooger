(ns auth.proxy.handler.exception
  (:require [integrant.core :as ig])
  (:import [io.undertow.server HttpHandler]
           [io.undertow.server.handlers  ExceptionHandler]
           [io.undertow.util AttachmentKey]))

(defn- handler [logger]
  (reify HttpHandler
    (handleRequest [this exchange]
      (let [;; TODO: cannot receive exception
            exception (.getAttachment exchange (AttachmentKey/create Throwable))]
        (println "exception!:" exception)))))

(defmethod ig/init-key :auth.proxy.handler/exception [_ {:keys [next-handler logger]}]
  (-> (ExceptionHandler. next-handler)
      (.addExceptionHandler Throwable (handler logger))))
