package model

import akka.actor.Actor
import akka.event.Logging
import java.sql.Timestamp

import com.typesafe.config.ConfigFactory
import dto._

trait AggregatorModel {

  def getNotEmptyTopics() = {
    val conf = ConfigFactory.load()
    val baseDir = conf.getString("base-dir")
    Topics(FileUtils.getTopicsNames(baseDir))
  }

  def getLastTimestamp(topic: String) = {
    // fake
    RunTimestamp(new Timestamp(2015, 12, 4, 18, 48, 0, 0))
  }

  def getLastStartStat(topic: String) = {
    // fake
    TimestampStat(10000, 5000, 1000, 2000)
  }

  def getPartitionsInfo(topic: String, timestamp: String) = {
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
