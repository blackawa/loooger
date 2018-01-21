(ns front.api.handler.logs
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.api.boundary.auth :as auth]))

(defmethod ig/init-key ::create [_ {:keys [redis]}]
  (fn [{{bearer-token "authorization"} :headers [_ account-id-from-url] :ataraxy/result :as req}]
    (let [token (last (clojure.string/split bearer-token #" "))
          account-id-from-token (auth/fetch-account-id redis token)]
      (if (or (nil? account-id-from-token)
              (not (= (str account-id-from-token) (str account-id-from-url))))
        [::response/unauthorized]
        (let [log-id "xxx"]
          [::response/created (str "/accounts" account-id-from-token "/logs/" log-id)])))))
