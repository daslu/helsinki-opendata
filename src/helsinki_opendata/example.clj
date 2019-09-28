(ns helsinki-opendata.example
  (:require [helsinki-opendata.data :as data]
            [clojure.java.browse :as browse]
            [clojuress :as r :refer [r]]
            [clojuress.packages.rmarkdown :as rmd]
            [clojuress.util :refer [-|> tilde]]
            [clojure.java.browse :as browse]
            [tech.ml.dataset :as dataset]))

;; To run this report,
;; you need R installed with the packages Rserve, leaflet.


(defn report [[lat lng] radius]
  (let [data (->> (data/cases-nearby [lat lng] radius)
                  (map #(select-keys % [:lat :lng :hanke_englanti])))]
    (->  [:div
          [:h3 "Cases nearby " (pr-str [lat lng])]
          [:r "provider<-providers$CartoDB.Positron"]
          (if (seq data)
            [:r-edn
             [:library :leaflet]
             (-|> [:leaflet :data]
                  [:addProviderTiles :provider]
                  [:addCircleMarkers
                   {:lat   (tilde :lat)
                    :lng   (tilde :lng)
                    :label (tilde :hanke_englanti)}]
                  [:addMarkers
                   {:lat lat
                    :lng lng}])
             :data]
            ;; else
            "No cases")]
         rmd/hiccup->rmd
         (rmd/render-rmd {:data (when (seq data)
                                  (dataset/->dataset
                                   data))})
         browse/browse-url)))



(comment
  (report [60 25] 2000)

  (report [60.1708156 24.9346307] 2000)
  )


