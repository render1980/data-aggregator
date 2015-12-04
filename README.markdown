# Data-aggregator

HTTP REST service written on akka-spray. It reads base (files hierarchy) with defined structure
and returns some information based on processing results.

## Base hierarchy
<pre>
topic_name[1]            .. topic_name[M]
|                        |
-- history               -- history
   |                        |
   -- run_timestamp1        -- run_timestamp[M1]
      |                        |
      -- offsets.csv           -- offsets.csv
   ..                       ..
   -- run_timestampN        -- run_timestamp[MK]
      |                        |
      -- offsets.csv           -- offsets.csv
</pre>

* M - amount of all topics
* N - amount of all timestamps for topic[1]
* K - amount of all timestamps for topic[M]

## Run

sbt clean compile package && java -jar ./targer/scala-2.11/data-aggregator_2.11-0.1.jar -Dbase_dir=[base_dir]
