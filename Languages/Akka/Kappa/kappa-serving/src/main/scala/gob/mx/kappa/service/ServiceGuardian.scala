package gob.mx.kappa.service

import akka.actor.{Actor, ActorLogging, Props}
import gob.mx.kappa.service.actors.HttpServiceActor

class ServiceGuardian extends Actor with ActorLogging{

  val serviceApi = context.actorOf(
    Props(new HttpServiceActor),
    "kappa-service-api"
  )

  def receive = {
    case _ =>
  }

}
