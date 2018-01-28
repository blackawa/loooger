(ns front.web.view.errors
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc not-found []
  (layout
   [:h1 "404 Resource not found"]
   [:a {:href "/"} "Go to top page and try again."]))

(rum/defc unauthorized []
  (layout
   [:h1.header "401 Unauthorized"]
   [:h2 "Thank you for access."]
   [:p "Currently, we restrict sign up because we are in closed-beta status."]
   [:p "We are happy if you contact us via our " [:a {:href "https://twitter.com/blackawa0"} "Twitter"]]))
