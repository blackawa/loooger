(ns front.web.client
  (:require [rum.core :as rum]
            [front.web.view.accounts :refer [logs]]))

(js/console.log "Connected")

(if-let [app (.getElementById js/document "app")]
  (do
    (.log js/console "mounting...")
    (rum/mount (logs) app))
  (.log js/console "cannot find element by id"))
