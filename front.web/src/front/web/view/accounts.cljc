(ns front.web.view.accounts
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc show []
  (layout
   [:h1 "Accounts#show"]
   [:input {:type "text" :placeholder "log here"}]
   [:script {:src "/js/main.js"}]))
