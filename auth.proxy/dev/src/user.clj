(ns user
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [auth.proxy.main :refer [config]]))

(integrant.repl/set-prep! #(config))
