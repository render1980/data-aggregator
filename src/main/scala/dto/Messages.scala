package dto

import java.sql.Timestamp

sealed trait RequestMessage
sealed trait ResultMessage
// Requests
case object GetTopics extends RequestMessage
case class GetTimestamp(topic: String) extends RequestMessage
case class GetStat(topic: String) extends RequestMessage
case class GetPartsInfo(topic: String, timestamp: String) extends RequestMessage
// Results
case class Topics(topics: List[String]) extends ResultMessage
case class RunTimestamp(timestamp: Timestamp) extends ResultMessage
case class TimestampStat(sum: Long, max: Long, min: Long, avg: Float) extends ResultMessage
case class PartsInfo(info: Map[Int, Long]) extends ResultMessage

case class Error(message: String)
case class Success(message: String) extends ResultMessage
