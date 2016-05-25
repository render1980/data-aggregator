#! /bin/bash -e

BASE_DIR=$1
BASE_DIR_DEFAULT=/var/tmp/agg_spool

if [ -z "$BASE_DIR" ]; then
  echo "::: Base dir is not defined. Using default value for it = $BASE_DIR_DEFAULT :::"
  BASE_DIR=$BASE_DIR_DEFAULT
fi

echo "::: Watching JAVA Version :::"
java -version
echo "::: Watching SBT Version :::"
sbt --version
echo "::: Start packaging project -> one-jar :::"
sbt clean one-jar
echo "::: Start service :::"
java -Dbase_dir=$BASE_DIR -jar target/scala-2.11/data-aggregator_*-one-jar.jar
