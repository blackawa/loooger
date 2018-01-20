(ns front.web.view.layout)

(defn layout [& body]
  [:html
   [:head
    [:title "Stream logs out of your mind | loooger"]]
   [:body body]])
