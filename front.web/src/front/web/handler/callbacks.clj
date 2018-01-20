(ns front.web.handler.callbacks
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]))

(defn- fetch-access-token [{:keys [client-id secret code state]}]
  )

(defmethod ig/init-key ::github [_ {:keys [db secrets]}]
  ;; get access token and store it
  ;; after get access token, fetch user name and register it to database.
  (fn [{{:keys [code state]} :params}]
    (let [client-id (get-in secrets [:github :id])
          secret (get-in secrets [:github :secret])
          access-token
          (fetch-access-token {:client-id client-id :secret secret
                               :code code :state state})
          name "foo"]
      [::response/see-other "/"])))
