(ns front.web.view.endpoint.logs
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(defn- fetch
  "fetch value from document.cookie"
  [k]
  (->> js/document
       .-cookie
       ((fn [source] (clojure.string/split source #"; ")))
       (map #(clojure.string/split % #"="))
       (filter (fn [source] (= k (first source))))
       first
       last))

(defn- url-decode [v]
  (js/decodeURIComponent (str v)))

(defn- fetch-token []
  (last (clojure.string/split (url-decode (fetch "ring-session")) #":")))

(defn create [request-body]
  (let [host front.web.client/endpoint]
    (http/post
     (str (.-protocol js/location) "//" host
          (.-pathname js/location) "/logs")
     {:oauth-token (fetch-token)
      :edn-params request-body})))
