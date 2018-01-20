(ns front.web.handler.accounts
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [front.web.util.auth :as auth]))

(defmethod ig/init-key ::show [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result session :session}]
    (if (auth/user-signed-in? session)
      [::response/ok (str id)]
      [::response/see-other "/"])))
