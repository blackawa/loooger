(ns front.web.view.endpoint.logs
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(defn- fetch
  "fetch value from document.cookie"
  [k]
  (->> js/document
       .-cookie
       (#(clojure.string/split % #"; "))
       (map #(clojure.string/split % #"="))
       (filter (fn [source] (= k (first source))))
       first
       last))

(defn- url-decode [v]
  (js/decodeURIComponent (str v)))

(defn- fetch-token []
  (url-decode (fetch "SESSION")))

(defn create [request-body]
  (http/post
   (str (.-protocol js/location) "//" (.-host js/location)
        "/api" (.-pathname js/location) "/logs")
   {:oauth-token (fetch-token)
    :edn-params request-body}))
