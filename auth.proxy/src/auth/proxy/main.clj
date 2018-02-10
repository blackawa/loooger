(ns auth.proxy.main
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [auth.proxy.reader.env :as env])
  (:gen-class))

(defn- add-shutdown-hook [system]
  (-> (Runtime/getRuntime)
      (.addShutdownHook (Thread. #(ig/halt! system)))))

(defn config []
  (->> (io/resource "auth/proxy/config.edn")
       (slurp)
       (ig/read-string {:readers {'env env/env}})))

(defn -main [& args]
  (-> (config)
      (ig/init [:auth.proxy.component/undertow])
      (add-shutdown-hook))
  (.. Thread currentThread join))
