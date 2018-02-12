(ns auth.proxy.handler.path
  (:require [integrant.core :as ig])
  (:import [io.undertow.server.handlers PathHandler]))

(defmethod ig/init-key :auth.proxy.handler/path [_ {:keys [paths]}]
  (let [path-handler (PathHandler.)]
    (doseq [[path handler] paths]
      (.addPrefixPath path-handler path handler))
    path-handler))
