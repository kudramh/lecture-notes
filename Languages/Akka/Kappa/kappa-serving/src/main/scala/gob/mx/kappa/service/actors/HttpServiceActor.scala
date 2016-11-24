package gob.mx.kappa.service.actors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import akka.http.scaladsl.Http.ServerBinding
import gob.mx.kappa.service.actors.utils.HttpRoutes
import scala.concurrent.Future

class HttpServiceActor extends Actor with ActorLogging {

  var httpListener:Future[ServerBinding] = null

  override def preStart(): Unit = {
    import HttpRoutes._
    implicit val materializer = ActorMaterializer()
    httpListener = Http(context.system).bindAndHandle(routes, "localhost", 8080)
  }

  def receive = {
    case _ =>
  }

}
