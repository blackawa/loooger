(ns front.web.view.accounts
  (:require [rum.core :as rum]
            [front.web.view.layout :refer [layout]]))

(defn db
  ([] (db {}))
  ([data] (atom data)))

(rum/defc log-form < rum/reactive [db]
  [:div
   [:select {:name "level"
             :on-change (fn [e] #?(:cljs (swap! db assoc-in [:form :level] (-> e .-target .-value))))}
    (map (fn [option] [:option {:value (:value option) :key (:value option)} (:label option)])
         [{:value :trace :label "TRACE"}
          {:value :debug :label "DEBUG"}
          {:value :info :label "INFO"}
          {:value :warn :label "WARN"}
          {:value :fatal :label "FATAL"}])]
   [:textarea {:placeholder "log here"
               :on-change (fn [e] #?(:cljs (swap! db assoc-in [:form :body] (-> e .-target .-value))))}]
   [:pre#preview (str "preview: [" (get-in (rum/react db) [:form :level]) "] " (get-in (rum/react db) [:form :body]))]
   [:button {:type "submit"
             :on-click (fn [_] #?(:cljs (.log js/console "clicked")))} "send"]])

(rum/defc recent-logs [db]
  ;; TODO: fetch logs
  (let [logs
        [{:id 1 :level :fatal :body "bodibodibodi" :created-at "2018-01-20 12:34:54"}
         {:id 2 :level :info :body "bodibodi" :created-at "2018-01-20 12:34:55"}
         {:id 3 :level :trace :body "bodi" :created-at "2018-01-20 12:34:56"}]]
    [:div [:ul (map (fn [log] [:li {:key (:id log)} (str log)]) logs)]]))

(rum/defc logs [db]
  [:div
   (recent-logs db)
   (log-form db)])
