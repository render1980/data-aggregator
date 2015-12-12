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

Topic_name may be any

### run_timestamp dir format

Example: 2015-12-04T13:00:00.000Z

### offsets.csv format
<pre>
[partition_id],[messages_amount]
part_id[1],messages_amount[1]
...
part_id[N],messages_amount[N]
</pre>

* partition_id - name of partition (or any other storage block)
* messages_amount - amount of messages for some partition (or any other storage block)

Names of columns may be any.

## Preconditions

Desribed early files hierarchy must be exists at some file path. By default base_dir parameter equal /var/tmp/agg_spool.

## Preinstalled soft requirements

* java (>= 1.7)
* sbt (>= 0.11)

Both must be available at $PATH variable

## Compile

```
sbt one-jar
```

## Run

### With sbt

```
sbt re-start
```

### With one-jar

```
sbt one-jar && java -Dbase_dir=/var/tmp/agg_spool -jar target/scala-2.11/data-aggregator_2.11-0.1-one-jar.jar
```

-Dbase_dir arg value must exclude slash at the end of file path!

### Or simply with start script

```
./start.sh
```

## HTTP API Testing

### Response headers

Server: spray-can/1.3.3

Date: [date]

Content-Type: application/json; charset=UTF-8

Content-Length: [content_length]

### Show all not empty topics

#### Request
```
curl http://127.0.0.1:8080/topics
```

#### Response
```
["topic1","topic2"]
```

### Show procedure start last time (in timestamp format) by topic

#### Request
```
curl http://127.0.0.1:8080/ts?topic=[topic_name]
```
#### Response
```
"2015-12-04T12:00:00.000Z"
```

### Show last execution statistics by topic

#### Request
```
curl http://127.0.0.1:8080/stat?topic=[topic_name]
```

#### Response
```
{"sum":2700,"max":1200,"min":100,"avg":675.0}
```

### Show partitions info by topic&timestamp

#### Request
```
curl http://127.0.0.1:8080/parts-info?topic=[topic_name]&timestamp=[required_timestamp]
```

#### Response
```
{"1":"100","2":"1000","3":"400","4":"1200"}
```



