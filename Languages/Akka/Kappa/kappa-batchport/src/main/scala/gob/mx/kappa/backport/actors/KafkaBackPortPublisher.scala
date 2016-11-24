package gob.mx.kappa.backport.actors


import java.util.Properties

import akka.actor.{Actor, ActorLogging}
import gob.mx.kappa.backport.BackPortSettings
import gob.mx.kappa.kafka.KappaProducer
import gob.mx.kappa.kafka.KappaEvent.{KappaMessage, RawEvent}

/**
  * Publishes Messages to Kafka on a sender's behalf.
  */
class KafkaBackPortPublisher(val producerConfig: Properties) extends Actor with ActorLogging with BackPortSettings {

  def this(host:String, key:String) = this(KappaProducer.createConfig(host,key))

  private val producer = new KappaProducer[String, String](producerConfig)

  override def postStop(): Unit = {
    log.info("Shutting down producer.")
    producer.close()
  }

  def receive = {
    case raw: RawEvent =>
      raw.messages.foreach(
        message => producer.send( new KappaMessage[String,String](raw.topic, KafkaKey, message) )
      )
  }

}
