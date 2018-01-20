(ns front.web.component.secrets
  (:require [integrant.core :as ig]))

(defmethod ig/init-key :front.web.component/secrets [_ args] args)
