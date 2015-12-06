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

### With sbt

```
sbt re-start
```

### With one-jar

```
java -Dbase_dir=/var/tmp/agg_spool -jar target/scala-2.11/data-aggregator_2.11-0.1-one-jar.jar
```

## Testing

### Show all not empty topics

```
curl http://127.0.0.1:8080/topics
```

### Show when procedire wa started (in timestamp format) by topic

```
curl http://127.0.0.1:8080/ts?topic=[topic_name]
```

### Show last execution statistics by topic

```
curl http://127.0.0.1:8080/stat?topic=[topic_name]
```

### Show partitions info by topic & timestamp

```
curl http://127.0.0.1:8080/parts-info?topic=[topic_name]&timestamp=[required_timestamp]
```