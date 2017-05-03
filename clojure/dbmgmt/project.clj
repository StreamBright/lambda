(defproject dbmgmt "0.1.0"
  :description "Updating materialized views in RDS"
  :url ""
  :license {
    :name ""
    :url ""
  }
  :dependencies [
    [org.clojure/clojure                  "1.8.0"     ]
    [org.postgresql/postgresql            "9.4.1212"  ]
    [org.clojure/data.json                "0.2.6"     ]
    [com.amazonaws/aws-lambda-java-core   "1.1.0"     ]
    [com.amazonaws/aws-lambda-java-log4j  "1.0.0"     ]
  ]
  :jvm-opts [
    "-Xms32m" "-Xmx32m"
    "-XX:+TieredCompilation" "-XX:TieredStopAtLevel=1"
    "-XX:+UseSerialGC"
    ;-XX:MaxHeapSize=222823k, -XX:MaxMetaspaceSize=26214k, -XX:ReservedCodeCacheSize=13107k, -XX:+UseSerialGC, -Xshare:on, -XX:-TieredCompilation
  ]
  :aot :all
  :main dbmgmt)
