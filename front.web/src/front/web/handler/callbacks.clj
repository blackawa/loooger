(ns front.web.handler.callbacks
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [front.web.boundary.github]
            [front.web.boundary.account :as account]))

(defmethod ig/init-key ::github [_ {:keys [db github]}]
  (fn [{{:keys [code state]} :params session :session}]
    (let [access-token
          (front.web.boundary.github/fetch-access-token
           github {:code code :state state})
          {name "login" github-account-id "id"}
          (front.web.boundary.github/fetch-account
           github {:access-token access-token})]
      (if (not (account/sign-up-permitted? db name))
        [::response/see-other "/signup_requests/new"]
        (let [signed-up? (account/signed-up-with-github? db github-account-id)
              account-id (if signed-up?
                           (:id signed-up?)
                           (account/create db {:name name
                                               :github_account_id github-account-id
                                               :access_token access-token
                                               :access_token_type :github}))]
          {:status 301
           :headers {"location" (str "/accounts/" account-id)}
           :session (assoc session :id account-id)})))))
