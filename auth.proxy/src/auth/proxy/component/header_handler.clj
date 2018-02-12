(ns auth.proxy.component.header-handler
  (:require [integrant.core :as ig]
            [auth.proxy.boundary.session-store :as session-store-boundary])
  (:import [io.undertow.server HttpHandler]
           [io.undertow.util HttpString]))

(def ^:private session-key "session")
(def ^:private header-key "X-Auth")

(defn- get-request-header [exchange k]
  (some-> exchange
          (.getRequestHeaders)
          (.get k)
          (.getFirst)))

(defn- set-request-header [exchange k v]
  (-> exchange
      (.getRequestHeaders)
      (.put (HttpString. k) v)))

(defn- get-response-header [exchange k]
  (some-> exchange
          (.getResponseHeaders)
          (.get k)
          (.getFirst)))

(defn- remove-response-header [exchange k]
  (-> exchange
      (.getResponseHeaders)
      (.remove k)))

(defn- get-cookie [exchange k]
  (some-> exchange
          .getRequestCookies
          (.get k)
          .getValue))

(defn- set-cookie [exchange k v]
  (-> exchange
      .getResponseCookies
      (.set k v)))

(defn- add-authorization-header
  "Add X-Auth header to internal request
   if request from external application contains
   Cookie or Authorization header."
  [exchange session-store]
  (let [authorization (some-> (get-request-header exchange "Authorization")
                              (clojure.string/replace #"Bearer\s" ""))
        session-id (get-cookie exchange session-key)
        session (session-store-boundary/get session-store (or authorization session-id))]
    (when session
      (set-request-header exchange header-key (str {:k session-id :v session})))
    exchange))

(defn- add-cookie [exchange session-store]
  (let [session (some-> (get-response-header exchange header-key)
                        read-string)
        session-id (or (:k session) (str (java.util.UUID/randomUUID)))]
    (when session
      (session-store-boundary/set session-store session-id (:v session))
      (set-cookie exchange session-key session-id)
      (remove-response-header exchange header-key))))

;; Accept from external connection
;;   - Cookies header
;;   - Authorization header
;; Send to internal application
;;   - X-Auth header
;; Accept from internal application
;;   - X-Auth header
(defmethod ig/init-key :auth.proxy.component/header-handler
  [_ {:keys [next-handler session-store]}]
  (reify HttpHandler
    (handleRequest [this exchange]
      (add-authorization-header exchange session-store)
      (.handleRequest next-handler exchange)
      (add-cookie exchange session-store))))
