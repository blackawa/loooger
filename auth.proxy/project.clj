(defproject auth.proxy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.6.3"]
                 [integrant/repl "0.3.0"]
                 [io.undertow/undertow-core "1.4.22.Final"]]
  :main ^:skip-aot auth.proxy.main
  :resource-paths ["resources"]
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["dev/src"]
                   :repl-options {:init-ns user}}
             :uberjar {:aot :all}})
