package service

import akka.actor.{Actor, Props}
import dto._
import model._
import org.json4s.DefaultFormats
import spray.http.MediaTypes._
import spray.httpx.Json4sSupport
import spray.routing.{HttpService, Route}

class AggServiceActor extends Actor with AggService with AggRequestCreator {
  implicit def actorRefFactory = context

  def receive = runRoute(aggRoute)

  def handleRequest(message: RequestMessage): Route = {
    ctx => aggRequest(ctx, Props[AggregatorActor], message)
  }
}

trait AggService extends HttpService with Json4sSupport {
  val json4sFormats = DefaultFormats

  val aggRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete(
            """<html>
              <body>
              <h1>Hellow!</h1><p>Here you can watching some metrics about procedures</p>
              <p><h3>Use:</h3></p>
              <ul>
              <li>/topics - show all not empty topics</li>
              <li>/ts?topic=[topic_name] - show last start timestamp by topic</li>
              <li>/stat?topic=[topic_name] - show last start statistics by topics</li>
              <li>/parts-info?topic=[topic_name]&amp;timestamp=[timestamp] - show partitions list with messages
              amount by each part for topic and timestamp</li>
              </ul>
              </body>
              </html>""".stripMargin.replaceAll("[\n\r]",""))
        }
      }
    } ~ path("topics") {
      get {
        rejectEmptyResponse {
          handleRequest(GetTopics)
        }
      }
    } ~ path("ts") {
      get {
        parameters('topic.as[String]) { topic =>
          handleRequest {
            GetTimestamp(topic)
          }
        }
      }
    } ~ path("stat") {
      get {
        parameters('topic.as[String]) { topic =>
          handleRequest(GetStat(topic))
        }
      }
    } ~ path("parts-info") {
      get {
        parameters('topic.as[String], 'timestamp.as[String]) {
          (topic, timestamp) =>
            handleRequest(GetPartsInfo(topic, timestamp))
        }
      }
    }

  def handleRequest(message: RequestMessage): Route
}