(ns front.api.component.secrets
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :front.api.component/secrets [_ args] args)
