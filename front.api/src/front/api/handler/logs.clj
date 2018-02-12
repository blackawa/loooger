(ns front.api.handler.logs
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.api.util.auth :as auth]
            [front.api.boundary.logs :as logs]))

(defmethod ig/init-key ::create [_ {elasticsearch :elasticsearch}]
  (fn [{headers :headers
        [_ url-account-id] :ataraxy/result
        log :body-params
        :as req}]
    (let [;; FIXME Cause NullPointerException when authorization header is not set!
          {{session-account-id :id} :v} (auth/session headers)]
      (println (auth/session headers))
      (if (or (nil? session-account-id)
              (not (= (str session-account-id) (str url-account-id))))
        [::response/unauthorized]
        (let [[errors created-log] (logs/create elasticsearch session-account-id log)]
          (if errors
            {:status 400 :body errors}
            (let [location (str "/accounts/" session-account-id "/logs/" (:id created-log))]
              [::response/created location])))))))
