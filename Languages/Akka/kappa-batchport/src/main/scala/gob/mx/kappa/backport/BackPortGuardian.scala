package gob.mx.kappa.backport

import akka.actor.{Actor, ActorLogging, Props}
import gob.mx.kappa.backport.actors.HttpBackPortActor
import gob.mx.kappa.backport.actors.KafkaBackPortPublisher

class BackPortGuardian extends Actor with ActorLogging with BackPortSettings{

  val kafkaStream = context.actorOf(
    Props(new KafkaBackPortPublisher(KafkaBrokers, KafkaKey)),
    "kappa-backport-kafka-publisher"
  )

  val htppListener = context.actorOf(
    Props(new HttpBackPortActor(kafkaStream, HttpHost, HttpPort)),
    "kappa-backport-http-listener"
  )

  def receive = {
    case _ =>
  }

}
