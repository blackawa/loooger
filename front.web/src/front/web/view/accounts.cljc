(ns front.web.view.accounts
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(rum/defc log-form []
  [:div
   [:select {:name "level"}
    (map (fn [option] [:option {:value (:value option) :key (:value option)} (:label option)])
         [{:value nil :label "choose level"}
          {:value :trace :label "TRACE"}
          {:value :debug :label "DEBUG"}
          {:value :info :label "INFO"}
          {:value :warn :label "WARN"}
          {:value :fatal :label "FATAL"}])]
   [:input {:type "text"
            :name "body"
            :placeholder "log here"
            :on-change (fn [e] #?(:clj (println e) :cljs (.log js/console "hello")))}]
   [:button {:type "submit"} "send"]])

(rum/defc recent-logs []
  ;; TODO: fetch logs
  (let [logs
        [{:id 3 :level :trace :body "bodi" :created-at "2018-01-20 12:34:56"}
         {:id 2 :level :info :body "bodibodi" :created-at "2018-01-20 12:34:55"}
         {:id 1 :level :fatal :body "bodibodibodi" :created-at "2018-01-20 12:34:54"}]]
    [:div [:ul (map (fn [log] [:li {:key (:id log)} (str log)]) logs)]]))

(rum/defc logs []
  [:div
   (recent-logs)
   (log-form)])

(rum/defc show []
  (layout
   [:h1 "Accounts#show"]
   [:div#app
    ;; (logs)
    ]
   [:script {:src "/js/main.js"}]))
