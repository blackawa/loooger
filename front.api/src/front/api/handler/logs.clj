(ns front.api.handler.logs
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]))

(defmethod ig/init-key ::create [_ _]
  (fn [req] [::response/created "/logs/xxx"]))
