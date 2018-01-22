(ns front.web.view.endpoint.logs
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(defn create [request-body]
  (.log js/console "sending request")
  (let [host front.web.client/endpoint]
    (http/post
     (str (.-protocol js/location) "//" host
          (.-pathname js/location) "/logs")
     {:oauth-token "xxx"
      :edn-params request-body})))
