package model

import java.io.File
import akka.actor.Actor
import akka.event.Logging
import com.github.nscala_time.time.Imports.{DateTime}
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
        .filter(f => !isChildDirEmpty(f, "history")).map(f => f.getName).toList)
  }

  def getLastTimestamp(topic: String) = {
    val conf = ConfigFactory.load()
    val f: File = new File(conf.getString("base-dir") + "/" + topic + "/history")
    val res: DateTime = getChildDirs(f) map(ts => dtFromIso8601(ts)) maxBy(t => t.getMillis)
    RunTimestamp(res.toString)
  }

  def getLastStartStat(topic: String) = {
    val conf = ConfigFactory.load()
    // fake
    TimestampStat(10000, 5000, 1000, 2000)
  }

  def getPartitionsInfo(topic: String, timestamp: String) = {
    val conf = ConfigFactory.load()
    // fake
    PartsInfo(Map(1 -> 5000, 2 -> 1000, 3 -> 1000,
      4 -> 1000, 5 -> 1000, 6 -> 1000))
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
