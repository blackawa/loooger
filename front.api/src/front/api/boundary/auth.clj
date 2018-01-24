(ns front.api.boundary.auth
  (:require [taoensso.carmine :as car]
            [front.api.component.redis]))

(defprotocol AuthClient
  (fetch-account-id [redis token]))

(extend-protocol AuthClient
  front.api.component.redis.Boundary
  (fetch-account-id [{conn :spec} token]
    (-> (car/wcar conn (car/get (str "carmine:session:" token))) :id)))
