(ns front.web.client
  (:require [rum.core :as rum]
            [front.web.view.accounts :refer [logs]]))

(js/console.log "Connected")

(def db (front.web.view.accounts/db))
(def debug? ^boolean js/goog.DEBUG)

;; mount react component
(if-let [app (.getElementById js/document "app")]
  (rum/mount (logs db) app)
  (.error js/console "cannot find element by id"))
