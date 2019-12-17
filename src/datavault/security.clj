(ns datavault.security
  (:require [datavault.db :as db]
            [clojure.data.json :as json]
            [buddy.sign.jwt :as jwt]
            [ring.util.response :refer [response]]
            [buddy.hashers :as hashers]))

(defonce secret "86bae26023208e57a5880d5ad644143c567fc57baaf5a942")

(defn bad-request [code message]
  {:status  400
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:status "failure"
                             :error  {:code    code
                                      :message message}})})

(defn ok [result]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str {:status "success"
                             :data   result})})

(defn generate-token [user]
  (jwt/sign {:user (:id user)} secret))

(defn check-password [user password]
  (hashers/check password (:password user)))

(defn get-field [req field]
  (let [body (:body req)]
    (get body field)))

(defn authenticate [req]
  (let [body (:body req)]
    (let [user (db/get-user-by-email (:email body))]
      (if (nil? user)
        (bad-request "error.authenticate" "Invalid username or password specified (Not found)")
        (if (check-password user (:password body))
          (response {:token (generate-token user)})
          (bad-request "error.authenticate" "Invalid username or password specified (Incorrect password)"))))))

