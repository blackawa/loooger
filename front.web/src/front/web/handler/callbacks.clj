(ns front.web.handler.callbacks
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.web.boundary.github]
            [front.web.boundary.account :as account]))

(defmethod ig/init-key ::github [_ {:keys [db github]}]
  ;; after get access token, fetch user name and register it to database.
  (fn [{{:keys [code state]} :params}]
    (let [access-token
          (front.web.boundary.github/fetch-access-token
           github {:code code :state state})
          {name "login" github-account-id "id"}
          (front.web.boundary.github/fetch-account
           github {:access-token access-token})
          signed-up? (account/signed-up-with-github? db github-account-id)
          account-id (if signed-up?
                       (:id signed-up?)
                       (account/create db {:name name
                                           :github_account_id github-account-id
                                           :access_token access-token
                                           :access_token_type :github}))]
      [::response/see-other (str "/accounts/" account-id)])))
