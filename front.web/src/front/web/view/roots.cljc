(ns front.web.view.roots
  (:require [rum.core :as rum]))

(rum/defc index [{:keys [github-authorization-url]}]
  [:html
   [:head]
   [:body
    [:h1 "Roots#index"]
    [:p
     [:a {:href github-authorization-url}
      "Sign in with GitHub"]]]])
