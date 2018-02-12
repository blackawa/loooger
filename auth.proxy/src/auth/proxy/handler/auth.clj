(ns auth.proxy.handler.auth
  (:require [integrant.core :as ig]
            [auth.proxy.boundary.session-store :as session-store-boundary]
            [auth.proxy.util.exchange :refer :all])
  (:import [io.undertow.server HttpHandler ResponseCommitListener]))

(def ^:private SESSION_KEY "SESSION")
(def ^:private HEADER_KEY "X-Auth")

(defn handler [{:keys [next-handler store]}]
  (reify HttpHandler
    (handleRequest [this exchange]
      ;; get cookie and session, and convert it to auth data.
      (let [authorization (some-> (get-request-header exchange "Authorization")
                                  (clojure.string/replace #"Bearer\s" ""))
            session-id (get-cookie exchange SESSION_KEY)
            session (session-store-boundary/get store (or authorization session-id))]
        (when session
          (set-request-header exchange HEADER_KEY (str {:k session-id :v session}))))
      ;; set listener to remove auth data and set cookie.
      (let [listener (reify ResponseCommitListener
                       (beforeCommit [this exchange]
                         (let [{:keys [k v]} (some-> (get-response-header exchange HEADER_KEY) read-string)]
                           (when (and k v)
                             ;; update session
                             (session-store-boundary/set store k v))
                           (when (and k (not v))
                             ;; discard session
                             (session-store-boundary/set store k)
                             (remove-cookie exchange SESSION_KEY))
                           (when (and (not k) v)
                             ;; create session
                             (let [session-id (str (java.util.UUID/randomUUID))]
                               (session-store-boundary/set store session-id v)
                               (set-cookie exchange SESSION_KEY session-id))))
                         (remove-response-header exchange HEADER_KEY)))]
        (.addResponseCommitListener exchange listener))
      (.handleRequest next-handler exchange))))
