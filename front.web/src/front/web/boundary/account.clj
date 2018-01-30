(ns front.web.boundary.account
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clojure.java.jdbc :as jdbc]
            [duct.database.sql]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]))

(defprotocol Account
  (create [db account])
  (signed-up-with-github? [db github-account-id]))

(extend-protocol Account
  duct.database.sql.Boundary
  (create [{db :spec} account]
    (let [now (time/now)
          account-seed (-> (select-keys account [:name])
                           (assoc :created_at now))
          access-token-seed (-> (select-keys account [:access_token :github_account_id])
                                (assoc :created_at now))]
      (jdbc/with-db-transaction [tx db]
        (->> (jdbc/insert! tx :accounts account-seed)
             first
             :id
             (assoc access-token-seed :account_id)
             (jdbc/insert! tx :account_github_access_tokens)
             first
             :account_id))))
  (signed-up-with-github? [{db :spec} github-account-id]
    (->> {:select [:accounts.*]
          :from [:accounts]
          :join [:account_github_access_tokens
                 [:= :accounts.id :account_github_access_tokens.account_id]]
          :where [:= :github_account_id github-account-id]}
         (sql/format)
         (jdbc/query db)
         first)))
