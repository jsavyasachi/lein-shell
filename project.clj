(defproject net.clojars.savya/lein-shell "1.0.2"
  :description "Call shell from within Leiningen."
  :url "https://github.com/jsavyasachi/lein-shell"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  ;; provided (non-transitive) so cljdoc can resolve leiningen.core.* for API docs
  :profiles {:provided {:dependencies [[leiningen-core "2.13.0"]]}})
