(ns auth.proxy.main
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [auth.proxy.component.undertow])
  (:gen-class))

(defn- add-shutdown-hook [system]
  (-> (Runtime/getRuntime)
      (.addShutdownHook (Thread. #(ig/halt! system)))))

(defn -main [& args]
  (-> (slurp (io/resource "auth/proxy/config.edn"))
      (ig/read-string)
      (ig/init [:auth.proxy.component/undertow])
      (add-shutdown-hook))
  (.. Thread currentThread join))
