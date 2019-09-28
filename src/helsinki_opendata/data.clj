(ns helsinki-opendata.data
  :require [helsinki-opendata.geo :as geo]
            [com.rpl.specter :as specter]
            [tech.ml.dataset :as dataset])


(def helsinki->world (geo/crs-transf 3879 4326))
(def world->helsinki (geo/crs-transf 4326 3879))

(def construction-data
  (-> "resources/Helsingin_kaupungin_rakennushankkeet_2018.csv"
      dataset/->dataset
      (dataset/->flyweight :error-on-missing-values? false)
      (->> (specter/transform [specter/ALL specter/MAP-KEYS] keyword)
           (map (fn [row]
                  (let [point (-> row
                                  ((juxt :p :i))
                                  geo/xy->point)
                        [lat lng] (-> point
                                      helsinki->world
                                      geo/point->xy)]
                    (assoc row
                           :point point
                           :lat lat
                           :lng lng)))))
      delay))

(def construction-index
  (-> @construction-data
      (geo/make-index :point)
      delay))

(defn cases-nearby [latlng radius]
  (let [center (-> latlng
                   geo/xy->point
                   world->helsinki)]
    (geo/rows-in-circle
     @construction-index
     center
     radius)))

