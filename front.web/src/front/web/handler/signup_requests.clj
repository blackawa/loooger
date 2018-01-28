(ns front.web.handler.signup-requests
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [rum.core :as rum]
            [front.web.view.errors :refer [unauthorized]]))

(defmethod ig/init-key ::new [_ _]
  (fn [_] [::response/unauthorized (rum/render-html (unauthorized))]))
