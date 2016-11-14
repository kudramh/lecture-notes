package gob.mx.kappa.backstream.actors

import java.util.concurrent.atomic.AtomicBoolean
import akka.actor.{Actor, ActorLogging, ActorRef}
import gob.mx.kappa.backstream.StreamMessages._
import kafka.serializer.StringDecoder
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.datastax.spark.connector.streaming._

class KafkaBackStreamActor(ssc:StreamingContext, kafkaParams:Map[String, String], listener:ActorRef) extends Actor with ActorLogging{

  protected val isRunning = new AtomicBoolean(false)
  var kafkaStream : ReceiverInputDStream[(String,String)] = null

  override def preStart(): Unit = {
    log.info("Starting up Kappa Consumer Actor.")
    kafkaStream = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder] (
      ssc, kafkaParams, Map("RawPort1" -> 1), StorageLevel.MEMORY_ONLY
    )
    kafkaStream
      .map( msg => parse( msg._2 ) )
      .map( jsonObject => (java.util.UUID.randomUUID.toString(), "a", "b") )
      .saveToCassandra("kappa_batch", "raw_data")
    listener ! OutputStreamInitialized
  }
  override def postStop(): Unit = {
    log.info("Shutting down Kappa Consumer Actor.")
  }

  private def stopConsuming(): Unit = {
    isRunning.set(false)
  }
  private def startConsuming(): Unit = {
    log.info("Kafka BackStrea Actor Started")
    isRunning.set(true)
  }

  def receive = {
    case StreamStart => startConsuming()
    case StreamStop  => stopConsuming()
  }

}
