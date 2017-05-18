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
    [java.net                               URLEncoder                            ]
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
  "returns {:ok string } or {:error...}"
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
       "&" "password=" (URLEncoder/encode (:password hm) "UTF-8")
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


;;;; needs to be rewritten

(defn exec-sql
  [db-connection statement]
  (try
    {:ok
      (get-fst-result
        (execute-query
          (create-statement db-connection)
          statement)) }
  (catch Exception e
    {:error "Exception" :fn "exec-sql" :exception (.getMessage e) })))

(defn start-time
  ^Date []
  (Date. (.getStartTime (ManagementFactory/getRuntimeMXBean))))

(defn get-input-args
  []
  (.getInputArguments (ManagementFactory/getRuntimeMXBean)))


(defn get-kms
  [key-name]
  (println (str "get-kms :: " key-name))
  (let [

                        encryptedKey  (Base64/decode (System/getenv key-name))
                        _             (println encryptedKey)
        ^AWSKMS         client        (AWSKMSClientBuilder/defaultClient)
                        _             (println client)
        ^DecryptRequest request       (.withCiphertextBlob (DecryptRequest.) (ByteBuffer/wrap encryptedKey))
                        _             (println request)
        ^ByteBuffer     plainTextKey  (.getPlaintext (.decrypt client request))
        ^String         ret           (String. (.array plainTextKey) (Charset/forName "UTF-8"))
                        _             (println (str "ret :: " ret))
        ]
    ret))

(defn get-edn-env
  [key-name]
  (edn/read-string (String. (Base64/decode (System/getenv key-name)))))

(defn config-selector
  "Works only with edn format either raw or base64 encoded"
  [config-type config-key]
  (println (str "Config selector :: " config-type " :: " config-key))
  (cond
    (= config-type :local)
      {:ok
        (:ok
          (parse-edn-string
            (:ok
              (read-file (File. (str "conf/" config-key ".edn")))))) } ; raw edn
    (= config-type :kms)
      {:ok (get-kms config-key)} ; base64 encoded edn? (not used due to AWS limitations)
    (= config-type :env)
      {:ok (get-edn-env config-key)} ; base64 encoded edn
    :else
      {:err :err}))

(def select-now "SELECT NOW();")

(defn db-helper
  [db-k-v]
  (let [  k (first (keys db-k-v))
          v (get-db-connection (get-in db-k-v [(keyword k)])) ]
    {k v}))

(defn get-db-connections
  [databases]
  (map #(db-helper %) (:ok databases)))

(defn sql-exec
  [config-type config-key] ; [:local|:kms|:env "eu_central_1"]
  (let [  databases   (config-selector config-type config-key)
          connz       (get-db-connections databases)
          nows-rs     (map #(exec-sql (first (vals %)) select-now) connz)
          now-results (map #(.getString (:ok %) "now") nows-rs) ]
    (println now-results)
    (map #(.close (first (vals %))) connz)))

(defn init
  []
  (println (str "Start time :: " (start-time)))
  (println (str "Input args :: " (get-input-args))))

(defn -handler
  [s]
  (println "Handler :: start...")
  (init)
  (let [region (System/getenv "region")]
    (sql-exec :env region)
    s))

(defn -main
  [& args]
  (println "Main :: start...")
  (init)
  (sql-exec :local)
  (println "Bye...")
  (System/exit 0))
