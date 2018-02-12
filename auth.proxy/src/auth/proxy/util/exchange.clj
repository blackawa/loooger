(ns auth.proxy.util.exchange
  (:import [io.undertow.server HttpServerExchange]
           [io.undertow.server.handlers CookieImpl]
           [io.undertow.util HttpString]))

(defprotocol Extension
  (get-request-header [this k])
  (set-request-header [this k v])
  (remove-request-header [this k])
  (get-response-header [this k])
  (set-response-header [this k v])
  (remove-response-header [this k])
  (get-cookie [this k])
  (set-cookie [this k v])
  (remove-cookie [this k]))

(extend-type HttpServerExchange
  Extension
  (get-request-header [this k]
    (some-> this
            (.getRequestHeaders)
            (.get k)
            (.getFirst)))
  (set-request-header [this k v]
    (some-> this
            (.getRequestHeaders)
            (.put (HttpString. k) v)))
  (remove-request-header [this k]
    (some-> this
            (.getRequestHeaders)
            (.remove k)))
  (get-response-header [this k]
    (some-> this
          (.getResponseHeaders)
          (.get k)
          (.getFirst)))
  (set-response-header [this k v]
    (some-> this
            (.getResponseHeaders)
            (.put (HttpString. k) v)))
  (remove-response-header [this k]
    (some-> this
            (.getResponseHeaders)
            (.remove k)))
  (get-cookie [this k]
    (some-> this
            .getRequestCookies
            (.get k)
            .getValue))
  (set-cookie [this k v]
    (some-> this
            .getResponseCookies
            (.put k (-> (CookieImpl. k) (.setValue v) (.setPath "/")))))
  (remove-cookie [this k]
    (some-> this
            .getResponseCookies
            (.remove k))))
