import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import service.AggServiceActor
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem("data-aggregator")
  implicit val timeout = Timeout(5.seconds)
  val service = system.actorOf(Props[AggServiceActor], "demo-service")

  val conf = ConfigFactory.load()
  val port = conf.getInt("port")
  val host = conf.getString("host")

  IO(Http) ? Http.Bind(service, interface = host, port = port)
}
