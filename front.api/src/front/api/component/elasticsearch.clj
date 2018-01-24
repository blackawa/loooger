(ns front.api.component.elasticsearch
  (:require [integrant.core :as ig]))

(defrecord Boundary [spec])

(defmethod ig/init-key :front.api.component/elasticsearch [_ {:keys [host port]}]
  (-> {:url (str "http://" (or host "127.0.0.1") ":" (or port "9200"))}
      (->Boundary)))
