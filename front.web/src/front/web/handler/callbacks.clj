(ns front.web.handler.callbacks
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.web.boundary.github]))

(defmethod ig/init-key ::github [_ {:keys [db github]}]
  ;; after get access token, fetch user name and register it to database.
  (fn [{{:keys [code state]} :params}]
    (let [access-token
          (front.web.boundary.github/fetch-access-token
           github {:code code :state state})
          name
          (front.web.boundary.github/fetch-account-name
           github {:access-token access-token})]
      (println access-token)
      [::response/ok "callback"])))
