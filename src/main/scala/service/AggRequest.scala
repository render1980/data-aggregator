package service

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import dto.{Error, RequestMessage, TimestampStat, _}
import org.json4s.DefaultFormats
import service.AggRequest.WithProps
import spray.http.StatusCode
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.RequestContext
import scala.concurrent.duration._

trait AggRequest extends Actor with Json4sSupport {
  def requestContext: RequestContext

  def target: ActorRef

  def message: RequestMessage

  import context._
  setReceiveTimeout(1.seconds)
  target ! message

  def receive = {
    case Topics(topics) => complete(OK, topics)
    case RunTimestamp(timestamp) => complete(OK, timestamp)
    case TimestampStat(sum, max, min, avg) => complete(OK, TimestampStat(sum, max, min, avg))
    case PartsInfo(info) => complete(OK, info)
    case Error(message) => complete(BadRequest, message)
    case ReceiveTimeout => complete(GatewayTimeout, "Request Timeout")
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
    requestContext.complete(status, obj)
    stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e => {
        complete(InternalServerError, Error(e.getMessage))
        Stop
      }
    }
}

object AggRequest {
  case class WithProps(requestContext: RequestContext, props: Props, message: RequestMessage) extends AggRequest {
    lazy val target = context.actorOf(props)

    implicit def json4sFormats = DefaultFormats
  }
}

trait AggRequestCreator {
  this: Actor =>

  def aggRequest(requestContext: RequestContext, props: Props, message: RequestMessage) = {
    context.actorOf(Props(new WithProps(requestContext, props, message)))
  }
}