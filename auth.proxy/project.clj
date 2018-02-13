(defproject auth.proxy "0.1.0-SNAPSHOT"
  :description "Reverse proxy server"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.6.3"]
                 [integrant/repl "0.3.0"]
                 [io.undertow/undertow-core "1.4.22.Final"]
                 [hikari-cp "2.2.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.postgresql/postgresql "42.1.4"]
                 [com.taoensso/carmine "2.17.0"]
                 [com.taoensso/timbre "4.10.0"]]
  :main ^:skip-aot auth.proxy.main
  :resource-paths ["resources"]
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["dev/src"]
                   :repl-options {:init-ns user}}
             :uberjar {:aot :all}})
