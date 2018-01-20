(ns front.web.view.roots
  (:require [rum.core :as rum]))

(rum/defc label [text]
  [:div {:class "label"} text])
