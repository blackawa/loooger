(ns front.web.view.roots
  (:require [rum.core :as rum]))

(rum/defc label [text]
  [:div {:class "label"} text])

(rum/defc index [{:keys [github-signin-url]}]
  [:html
   [:head]
   [:body
    [:h1 "Roots#index"]
    [:p
     [:a {:href github-signin-url} "Sign in with GitHub"]
     [:input {:type "text" :value "hoge-"}]]]])
