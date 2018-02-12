(ns front.api.handler.logs
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.api.boundary.auth :as auth]
            [front.api.boundary.logs :as logs]))

(defmethod ig/init-key ::create [_ {{host :host} :secrets
                                    redis :redis
                                    elasticsearch :elasticsearch
                                    :as deps}]
  (fn [{{bearer-token "authorization"} :headers
        [_ account-id-from-url] :ataraxy/result
        log :body-params
        :as req}]
    (let [;; FIXME Cause NullPointerException when authorization header is not set!
          token (last (clojure.string/split bearer-token #" "))
          account-id-from-token (auth/fetch-account-id redis token)]
      (if (or (nil? account-id-from-token)
              (not (= (str account-id-from-token) (str account-id-from-url))))
        [::response/unauthorized]
        (let [[errors created-log] (logs/create elasticsearch account-id-from-token log)]
          (if errors
            {:status 400 :body errors}
            (let [location (str "/accounts/" account-id-from-token "/logs/" (:id created-log))]
              [::response/created location])))))))
