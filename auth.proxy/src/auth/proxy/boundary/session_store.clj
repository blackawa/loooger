(ns auth.proxy.boundary.session-store
  (:require [taoensso.carmine :as car]))

(defn get [conn k]
  (car/wcar conn (car/get k)))

(defn set [conn k v]
  (car/wcar conn (car/set k v)))
