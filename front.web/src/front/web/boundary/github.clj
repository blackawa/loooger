(ns front.web.boundary.github
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [ring.util.codec :refer [url-encode url-decode]]
            [front.web.component.github]))

(defprotocol GithubClient
  (fetch-authorization-url [github options])
  (fetch-access-token [github options])
  (fetch-account-name [github options]))

(extend-protocol GithubClient
  front.web.component.github.Boundary
  (fetch-authorization-url [{:keys [client-id]} {:keys [host]}]
    (let [redirect-url (str host "/callback/github")]
      (format
       "https://github.com/login/oauth/authorize?client_id=%s&redirect_url=%s&scope=%s&state=%s"
       (url-encode client-id)
       (url-encode redirect-url)
       (url-encode "read:user")
       (url-encode (str (java.util.UUID/randomUUID))))))
  (fetch-access-token [{:keys [client-id secret]} {:keys [code state]}]
    (let [response-body
          (:body (http/post "https://github.com/login/oauth/access_token"
                            {:form-params {:client_id client-id :client_secret secret
                                           :code code :state state}
                             :accept "application/json"}))]
      (->> (get (json/read-str response-body) "access_token"))))
  (fetch-account-name [github {:keys [access-token]}]
    "fuga"))
