(defproject helsinki-opendata "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :repositories [["clojars"                                      {:url "https://clojars.org/repo/"}]
                 ["maven-central"                                {:url "https://repo1.maven.org/maven2"}]
                 ["codice-releases"
                  {:url "https://artifacts.codice.org/content/repositories/releases/"}]
                 ["Boundless Maven Repository"                   {:url "https://repo.boundlessgeo.com/main"}]
                 ["Open Source Geospatial Foundation Repository" {:url "https://download.osgeo.org/webdav/geotools"}]]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.locationtech.jts/jts-core "1.16.1"]
                 [org.locationtech.jts.io/jts-io-common "1.16.1"]
                 [org.codice.thirdparty/geotools-suite "20.1_1"]
                 [techascent/tech.ml.dataset "1.25"]
                 [alembic "0.3.2"]
                 [com.rpl/specter "1.1.3-SNAPSHOT"]
                 [scicloj/clojuress "0.1.0-SNAPSHOT"]])
