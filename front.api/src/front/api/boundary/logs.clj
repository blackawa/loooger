(ns front.api.boundary.logs
  (:require [clj-http.client :as http]
            [front.api.component.elasticsearch]
            [clojure.data.json :as json]))

(defprotocol Logs
  (create [elasticsearch account-id log]))

(extend-protocol Logs
  front.api.component.elasticsearch.Boundary
  (create [{{url :url} :spec} account-id log]
    (let [response (http/post (str url "/" account-id "/logs")
                              {:form-params log
                               :content-type :json})]
      (if-let [log-id (-> response
                          :body
                          json/read-str
                          (get "_id"))]
        [nil (assoc log :id log-id)]
        [{:erros (:body response)} log]))))
