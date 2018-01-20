(ns front.web.handler.roots
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.web.view.roots :as view]
            [rum.core :as rum]
            [front.web.boundary.github :as github]))

(defmethod ig/init-key ::index [_ {github :github secrets :secrets}]
  (fn [req]
    [::response/ok
     (rum/render-html
      (view/index
       {:github-authorization-url
        (github/fetch-authorization-url
         github (select-keys secrets [:host]))}))]))
