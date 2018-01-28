(ns front.web.view.layout)

(defn layout [& body]
  [:html
   [:head
    [:title "Stream logs out of your mind | loooger"]
    [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.2/css/bulma.min.css"}]
    [:link {:rel "stylesheet" :href "https://use.fontawesome.com/releases/v5.0.4/css/all.css"}]
    [:link {:rel "stylesheet" :href "/css/main.css"}]]
   [:body
    [:section.hero.is-primary
     [:div.hero-body
      [:div.container
       [:nav.has-text-centered
        [:h1.title "loooger"]
        [:h2.subtitle "Spit out your mind"]]]]]
    [:div.container
     body]]])

(defn static-layout [app]
  (str
   "<!doctype html5>"
   "<html>"
   "<head><title>Stream logs out of your mind | loooger</title><link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.2/css/bulma.min.css\"><link href=\"https://use.fontawesome.com/releases/v5.0.4/css/all.css\" rel=\"stylesheet\"><link rel=\"stylesheet\" href=\"/css/main.css\"></head>"
   "<body>"
   "<div class=\"container\">"
   "<div id=\"app\">"
   app
   "</div>"
   "</div>"
   "<script src=\"/js/main.js\"></script>"
   "</body></html>"))
