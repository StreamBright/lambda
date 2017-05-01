(ns dbmgmt
  (:require 
    [clojure.edn          :as     edn    ]
    [clojure.data.json    :as     json   ]
    [clojure.java.io      :as     io     ])
  (:import
    [com.amazonaws.services.lambda.runtime  RequestHandler Context LambdaLogger  ]
    [clojure.lang                           PersistentHashMap PersistentArrayMap ]
    [java.sql                               Connection DriverManager 
                                            SQLException Statement ResultSet     ]
    [java.util                              Properties Date                      ]
    [java.io                                File BufferedReader                  ]
    [java.lang.management                   ManagementFactory  RuntimeMXBean     ])
  (:gen-class 
    :implements [com.amazonaws.services.lambda.runtime.RequestHandler]
    :methods [[handleRequest [Object com.amazonaws.services.lambda.runtime.Context] String]]))

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

(def db-prod-eu 
  (:eu 
    (:ok 
      (parse-edn-string 
        (:ok 
          (read-file (File. "conf/prod.edn")))))))

(defn get-jdbc-url 
  ^String [^PersistentArrayMap hm] 
  (str "jdbc:" (:dbtype hm) "://" (:host hm) "/" 
       (:dbname hm) "?" "user=" (:user hm) 
       "&" "password=" (:password hm) 
       "&" "ssl=" (get-in hm [:ssl] "false")
       "&" "sslfactory=" (get-in hm [:sslfactory] "org.postgresql.ssl.NonValidatingFactory")))

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

(defn exec-sql 
  []
  (try 
    (get-fst-result 
      (execute-query 
        (.createStatement (connect-to-db (get-jdbc-url db-prod-eu))) 
        "SELECT NOW();"))
  (catch Exception e (str "Exception: " (.getMessage e)))))

(defn start-time 
  ^Date []
  (Date. (.getStartTime (ManagementFactory/getRuntimeMXBean))))

(defn get-input-args
  []
  (.getInputArguments (ManagementFactory/getRuntimeMXBean)))

(defn -handleRequest ^String [this in ctx]
  (reify RequestHandler
    (handleRequest ^String [this in ctx]
      (let [logger (.getLogger ctx)]
        (.log (str "Start time :: " (start-time)))
        (println (str "Start time :: " (start-time)))
        (.log (str "Input args :: " (get-input-args)))
        (println (str "Input args :: " (get-input-args)))
        (.log logger (str "now :: " (.getString (exec-sql) "now")))
        (println logger (str "now :: " (.getString (exec-sql) "now"))))
      "ok")))

(defn -main [& args]
  (println (str "Start time :: " (start-time)))
  (println (str "Input args :: " (get-input-args)))
  (println (str "now :: " (.getString (exec-sql) "now"))))
