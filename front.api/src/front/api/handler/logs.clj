(ns front.api.handler.logs
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.api.boundary.auth :as auth]))

(defmethod ig/init-key ::create [_ {{host :host} :secrets redis :redis}]
  (fn [{{bearer-token "authorization"} :headers [_ account-id-from-url] :ataraxy/result :as req}]
    (let [;; FIXME Cause NullPointerException when authorization header is not set!
          token (last (clojure.string/split bearer-token #" "))
          account-id-from-token (auth/fetch-account-id redis token)]
      (if (or (nil? account-id-from-token)
              (not (= (str account-id-from-token) (str account-id-from-url))))
        [::response/unauthorized]
        (let [log-id "xxx"]
          {:status 201
           :headers {:location (str "/accounts" account-id-from-token "/logs/" log-id)
                     "Access-Control-Allow-Origin" host}}
          [::response/created ])))))

(defmethod ig/init-key ::permit-post [_ {{host :host} :secrets}]
  (fn [req]
    {:status 200 :body ""
     :headers {"Access-Control-Allow-Methods" "POST"
               "Access-Control-Allow-Origin" host
               "Access-Control-Allow-Headers" "Authorization, Content-Type"
               "Access-Control-Allow-Credentials" "true"}}))
