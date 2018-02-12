(ns front.web.handler.accounts
  (:require [integrant.core :as ig]
            [ataraxy.response :as response]
            [rum.core :as rum]
            [front.web.util.auth :as auth]
            [front.web.view.accounts :as view]
            [front.web.view.errors :as error-view]
            [front.web.view.layout :refer [static-layout]]))

(defmethod ig/init-key ::show [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result headers :headers}]
    (let [session (auth/session headers)]
      (if (nil? session)
        [::response/see-other "/"]
        (if (not (= (str id) (str (get-in session [:v :id]))))
          [::response/not-found (rum/render-html (error-view/not-found))]
          [::response/ok (static-layout (rum/render-html (view/logs (view/db))))])))))
