package model

import java.io.{File}
import akka.actor.Actor
import akka.event.Logging
import com.typesafe.config.ConfigFactory
import dto._
import FileUtils._
import DateUtils._

trait AggregatorModel {

  def getNotEmptyTopics() = {
    val conf = ConfigFactory.load()
    val f: File = new File(conf.getString("base-dir"))
    Topics(
      f .listFiles
        .filter(f => !isChildDirEmpty(f, conf.getString("history-dir")))
        .map(f => f.getName)
        .toList)
  }

  def getLastTs(f: File): String = {
    getChildDirs(f) map(ts => dtFromIso8601(ts)) maxBy(t => t.getMillis) toString
  }

  def getLastTimestamp(topic: String) = {
    val conf = ConfigFactory.load()
    RunTimestamp(
      getLastTs(
        new File(conf.getString("base-dir") + "/"
          + topic + "/" + conf.getString("history-dir"))))
  }

  def getLastStartStat(topic: String) = {
    val rows = getOffsetsFileRows(topic)
    val amounts = rows map(m => m.values.head.toInt)
    val sum = amounts.sum
    TimestampStat(sum, amounts.max, amounts.min, sum / amounts.size)
  }

  def getPartitionsInfo(topic: String, timestamp: String) = {
    val rows = getOffsetsFileRows(topic)
    PartsInfo(rows reduce(_ ++ _))
  }

  def getOffsetsFileRows(topic: String): List[Map[String, String]] = {
    val conf = ConfigFactory.load()
    val f = new File(conf.getString("base-dir") + "/" + topic
      + "/" + conf.getString("history-dir"))
    val lastTs = getLastTs(f)
    val fOffsets = new File(f.getAbsolutePath + "/" + lastTs + "/"
      + conf.getString("csv-file"))
    readCsvFile(fOffsets, conf.getBoolean("csv-header"))
  }
}

class AggregatorActor extends Actor with AggregatorModel {
  var log = Logging(context.system, this)

  def receive = {
    case GetTopics => sender ! getNotEmptyTopics()
    case GetTimestamp(topic) => sender ! getLastTimestamp(topic)
    case GetStat(topic) => sender ! getLastStartStat(topic)
    case GetPartsInfo(topic, timestamp) => sender ! getPartitionsInfo(topic, timestamp)
    case _ => log.info("Message of unknown type has received")
  }
}
