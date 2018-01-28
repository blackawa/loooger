(ns front.web.view.roots
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc index [{:keys [github-authorization-url]}]
  (layout
   [:section.has-text-centered
    [:a.button.is-black {:href github-authorization-url}
     [:span.icon
      [:i.fab.fa-github]]
     [:span "Sign in with GitHub"]]]))
