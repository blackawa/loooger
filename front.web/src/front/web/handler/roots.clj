(ns front.web.handler.roots
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.web.view.roots :as view]
            [rum.core :as rum]))

(defmethod ig/init-key ::index [_ {:keys [secrets]}]
  (fn [req]
    [::response/ok (rum/render-html (view/label "hello"))]))
