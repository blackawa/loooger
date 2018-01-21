(ns front.web.view.errors
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc not-found []
  (rum/render-html
   (layout
    [:h1 "404 Resource not found"]
    [:a {:href "/"} "Go to top page and try again."])))
