(ns front.web.boundary.github
  (:require [clj-http.client :as http]
            [ring.util.codec :refer [url-encode]]
            [front.web.component.github]))

(defprotocol GithubClient
  (fetch-authorization-url [github options])
  (fetch-access-token [github])
  (fetch-account-name [github access-token]))

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
  (fetch-access-token [github code state] "hoge")
  (fetch-account-name [github access-token] "fuga"))
