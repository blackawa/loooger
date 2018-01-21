(ns front.web.view.layout)

(defn layout [& body]
  [:html
   [:head
    [:title "Stream logs out of your mind | loooger"]]
   [:body body]])

(defn static-layout [app]
  (str
   "<!doctype html5><html><head></head><body><div id=\"app\">"
   app
   "</div><script src=\"/js/main.js\"></script></body></html>"))
