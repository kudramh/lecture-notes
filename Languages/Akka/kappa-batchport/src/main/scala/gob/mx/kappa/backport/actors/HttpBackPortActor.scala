package gob.mx.kappa.backport.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpEntity
import akka.util.Timeout
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}

import scala.concurrent.duration._
import akka.http.scaladsl.unmarshalling.Unmarshal
import gob.mx.kappa.kafka.KappaEvent.RawEvent

class HttpBackPortActor(kafka: ActorRef, HttpHost:String, HttpPort:Int) extends Actor with ActorLogging {

  implicit val system = context.system
  implicit val executionContext = system.dispatcher
  implicit val askTimeout: Timeout = 500.millis
  implicit val materializer = ActorMaterializer(
    ActorMaterializerSettings(system)
  )

  // Low-Level API Implementation
  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.POST, Uri.Path("/backport/raw/data"), headers, entity, _) =>
      val rawData = Unmarshal(entity).to[String]
      rawData onSuccess {
        case msg =>
          kafka ! new RawEvent("RawPort1", msg)
      }
      HttpResponse(200, entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, "BackPort Raw Data Received."))
    case _: HttpRequest =>
      HttpResponse(400, entity = "Unsupported request")
  }
  Http(system).bindAndHandleSync(requestHandler, HttpHost, HttpPort)

  def receive = {
    case a => println(a)
  }

}
