(ns front.web.view.roots
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc index [{:keys [github-authorization-url]}]
  (layout
   [:h1 "Roots#index"]
   [:p
    [:a {:href github-authorization-url}
     "Sign in with GitHub"]]))
