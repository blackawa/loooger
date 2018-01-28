(ns front.web.view.errors
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc not-found []
  (layout
   [:h1 "404 Resource not found"]
   [:a {:href "/"} "Go to top page and try again."]))

(rum/defc unauthorized []
  (layout
   [:h1 "401 Unauthorized"]
   [:p "Thank you for access, but sign up is now restricted because we are beta-testing."
    [:a {:href "https://twitter.com/blackawa0"} "Contact me if you want an account."]]))
