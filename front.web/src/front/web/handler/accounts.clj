(ns front.web.handler.accounts
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [rum.core :as rum]
            [front.web.util.auth :as auth]
            [front.web.view.accounts :as view]
            [front.web.view.errors :as error-view]))

(defmethod ig/init-key ::show [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result session :session}]
    (if (not (auth/user-signed-in? session))
      [::response/see-other "/"]
      (if (not (= (str id) (str (:id session))))
        [::response/not-found (rum/render-html (error-view/not-found))]
        [::response/ok (rum/render-html (view/show))]))))
