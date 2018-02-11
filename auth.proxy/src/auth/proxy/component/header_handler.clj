(ns auth.proxy.component.header-handler
  (:require [integrant.core :as ig]
            [auth.proxy.boundary.session-store :as session-store-boundary])
  (:import [io.undertow.server HttpHandler]
           [io.undertow.util HttpString]))

(def ^:private session-key "session")
(def ^:private header-key "X-Auth")

(defn- get-request-header [exchange k]
  (-> exchange
      .getRequestHeaders
      (.get k)
      (#(when % (.getFirst %)))))

(defn- response-header [exchange k]
  (-> exchange
      .getResponseHeaders
      (.get "Authorization")
      (#(when % (.getFirst %)))))

(defn- get-cookie [exchange k]
  (-> exchange
      .getRequestCookies
      (.get k)
      (#(when % (.getValue %)))))

(defn- set-cookie [exchange k v]
  (-> exchange
      .getResponseCookies
      (.set k v)))

(defn- add-authorization-header
  "Add X-Auth header to internal request
   if request from external application contains
   Cookie or Authorization header."
  [exchange session-store]
  (let [authorization (-> (get-request-header exchange "Authorization")
                          (#(when % (clojure.string/replace % #"Bearer\s" ""))))
        session-id (get-cookie exchange session-key)
        session (session-store-boundary/get session-store (or authorization session-id))]
    (when session
      (-> exchange
          .getRequestHeaders
          (.put (HttpString. header-key) (str session))))))

(defn- add-cookie [exchange session-store]
  (let [session (response-header exchange header-key)
        session-id (or (get-cookie exchange session-key) (str (java.util.UUID/randomUUID)))]
    (when session
      (session-store-boundary/set session-store session-id session)
      (set-cookie exchange session-key session-id)
      (-> exchange
          .getResponseHeaders
          (.remove header-key)))))

;; Accept from external connection
;;   - Cookies header
;;   - Authorization header
;; Send to internal application
;;   - X-Auth header
;; Accept from internal application
;;   - X-Auth header
(defmethod ig/init-key :auth.proxy.component/header-handler
  [_ {:keys [next-handler database session-store]}]
  (reify HttpHandler
    (handleRequest [this exchange]
      (add-authorization-header exchange session-store)
      (.handleRequest next-handler exchange)
      (add-cookie exchange session-store))))
