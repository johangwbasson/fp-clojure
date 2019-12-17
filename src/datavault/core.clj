(ns datavault.core
  (:use ring.middleware.json)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :refer :all]
            [datavault.security :as sec])
  (:gen-class))

(defroutes app-routes
  (POST "/authenticate" []  sec/authenticate)
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true :bigdecimals? true})))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "7125"))]
    (server/run-server (wrap-defaults #'app api-defaults) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))