(ns dbmgmt
  (:require
    [clojure.edn          :as     edn    ]
    [clojure.data.json    :as     json   ]
    [clojure.java.io      :as     io     ])
  (:import
    [clojure.lang                           PersistentHashMap PersistentArrayMap  ]
    [java.sql                               Connection DriverManager
                                            SQLException Statement ResultSet      ]
    [java.util                              Properties Date                       ]
    [java.io                                File BufferedReader                   ]
    [java.nio                               ByteBuffer                            ]
    [java.nio.charset                       Charset                               ]
    [com.amazonaws.services.kms             AWSKMS AWSKMSClientBuilder            ]
    [com.amazonaws.services.kms.model       DecryptRequest                        ]
    [com.amazonaws.services.lambda.runtime  Context                               ]
    [com.amazonaws.util                     Base64                                ]
    [java.lang.management                   ManagementFactory  RuntimeMXBean      ])
  (:gen-class
    :methods [^:static [handler [String] String]]))

;; misc

(defn system-properties
  "Returns system-properties as a hashmap {}"
  ^PersistentHashMap []
  (reduce (fn [x [y z]] (assoc x y z)) {} (System/getProperties)))

(defn read-file
  "Returns {:ok string } or {:error...}"
  ^PersistentArrayMap [^File file]
  (try
    (cond
      (.isFile file)
        {:ok (slurp file) }
      :else
        (throw (Exception. "Input is not a file")))
  (catch Exception e
    {:error "Exception" :fn "read-file" :exception (.getMessage e) })))

(defn parse-edn-string
  "Returns {:ok {} } or {:error...}"
  ^PersistentArrayMap [^String s]
  (try
    {:ok (edn/read-string s)}
  (catch Exception e
    {:error "Exception" :fn "parse-config" :exception (.getMessage e)})))

;; db

;; nil if enything goes sideways

; (def db-prod-eu
;   (:eu
;     (:ok
;       (parse-edn-string
;         (:ok
;           (read-file (File. "conf/prod.edn")))))))

(defn get-jdbc-url
  ^String [^PersistentArrayMap hm]
  (str "jdbc:" (:dbtype hm) "://" (:host hm) "/"
       (:dbname hm) "?" "user=" (:user hm)
       "&" "password=" (:password hm)
       "&" "ssl=" (get-in hm [:ssl] "false")
       "&" "sslfactory=" (get-in hm [:sslfactory] "org.postgresql.ssl.NonValidatingFactory")))

(defn disconnect-from-db
  [^Connection db-connection]
  (.close db-connection))

(defn connect-to-db
  ^Connection [^String dburl]
  (DriverManager/getConnection dburl))

(defn create-statement
  ^Statement [^Connection dbconn]
  (.createStatement dbconn))

(defn execute-query
  ^ResultSet [^Statement statement ^String query]
  (.executeQuery statement query))

(defn get-fst-result
  [^ResultSet res]
  (if (.next res) res :err))

(defn get-db-connection
  [db]
  (connect-to-db (get-jdbc-url db)))

(defn exec-sql
  [db-connection statement]
  (try
    {:ok
      (get-fst-result
        (execute-query
          (.createStatement db-connection statement))) }
  (catch Exception e
    {:error "Exception" :fn "exec-sql" :exception (.getMessage e) })))

(defn start-time
  ^Date []
  (Date. (.getStartTime (ManagementFactory/getRuntimeMXBean))))

(defn get-input-args
  []
  (.getInputArguments (ManagementFactory/getRuntimeMXBean)))

(def select-now "SELECT NOW();")

(defn get-kms
  [key-name]
  (println key-name)
  (let [
        ^bytes          encryptedKey  (Base64/decode
                                        (System/getenv key-name))
        ^AWSKMS         client        (AWSKMSClientBuilder/defaultClient)
        ^DecryptRequest request       (.withCiphertextBlob
                                        (DecryptRequest.)
                                        (ByteBuffer/wrap encryptedKey))
        ^ByteBuffer     plainTextKey  (.getPlaintext
                                        (.decrypt client request))
        ^String         ret           (String.
                                        (.array plainTextKey) (Charset/forName "UTF-8"))
        ]
    ret))

; (fv [param] body)

(defn init
  []
  (println (str "Start time :: " (start-time)))
  (println (str "Input args :: " (get-input-args))))

(defn config-selector
  [config]
  (cond
    (= config :local)
      { :ok (parse-edn-string
              (:ok
                (read-file (File. "conf/prod.edn")))) }
    (= config :kms)
      { :ok (get-kms "prod") }
    :else
      { :err :err }))


(defn sql-exec
  [config] ; :local or :kms
  (let [ databases (config-selector config) ]
    (println databases)))

(defn -handler
  [s]
  (init)
  (sql-exec :kms)
  s)

(defn -main
  [& args]
  (init)
  (sql-exec :local))
