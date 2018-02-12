(ns auth.proxy.handler.web
  (:require [integrant.core :as ig]
            [auth.proxy.handler.auth :as auth])
  (:import [io.undertow.server.handlers.proxy ProxyHandler LoadBalancingProxyClient]
           [java.net URI]))

(defmethod ig/init-key :auth.proxy.handler/web [_ {:keys [backend-path store]}]
  (let [proxy-client (-> (LoadBalancingProxyClient.)
                         (.addHost (URI. backend-path)))]
    (auth/handler
     {:next-handler (-> (ProxyHandler/builder) (.setProxyClient proxy-client) (.build))
      :store store})))
