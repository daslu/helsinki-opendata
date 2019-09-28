(ns helsinki-opendata.geo
  (:import (org.geotools.referencing CRS)
           (org.geotools.geometry.jts JTS)
           (org.locationtech.jts.geom Coordinate Point Geometry GeometryFactory)
           (org.locationtech.jts.geom.prep PreparedGeometry PreparedGeometryFactory)
           (org.locationtech.jts.index.strtree STRtree)))

(defonce ^GeometryFactory geom-factory (GeometryFactory.))
(defonce ^PreparedGeometryFactory prep-geom-factory (PreparedGeometryFactory.))

(defn ->index []
  (STRtree.))

(defn insert! [index row ^PreparedGeometry prepared-geometry]
  (-> ^STRtree index
      (.insert (.getEnvelopeInternal ^Geometry (.getGeometry prepared-geometry))
               row)))

(defn build! [^STRtree index]
  (.build index))

(defn prepare-geometry [^Geometry geom]
  (PreparedGeometryFactory/prepare geom))

(defn make-index
  [rows row->geometry]
  (let [index (->index)]
    (doseq [row rows]
      (->> row
           row->geometry
           prepare-geometry
           (insert! index row)))
    (build! index)
    index))

(defn rows-in-circle [index ^Point center-point radius]
  (->> (.buffer center-point radius)
       (.getEnvelopeInternal )
       (.query ^STRtree index)
       ;; (filter (fn [geom1 geom2]
       ;;           (.intersects ^PreparedGeometry)))
       ))


(defn point->xy [^Point p]
  (let [coord (.getCoordinate p)]
    [(.x coord) (.y coord)]))

(defn xy->point [[x y]]
  (.createPoint geom-factory
                (Coordinate. x y)))


(def crs-transf
  (memoize
   (fn [source-crs target-crs]
     (let [transf (CRS/findMathTransform (->> source-crs
                                            (str "EPSG:")
                                            CRS/decode)
                                       (->> target-crs
                                            (str "EPSG:")
                                            CRS/decode))]
     (fn [^Geometry geometry]
       (JTS/transform geometry transf))))))

