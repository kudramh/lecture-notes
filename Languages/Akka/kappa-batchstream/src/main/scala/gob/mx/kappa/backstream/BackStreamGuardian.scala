package gob.mx.kappa.backstream

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import gob.mx.kappa.backstream.actors.KafkaBackStreamActor
import gob.mx.kappa.backstream.StreamMessages._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

class BackStreamGuardian extends Actor with ActorLogging with BackStreamSettings{

  var conf:SparkConf = null
  var ssc:StreamingContext = null
  var kafkaStream:ActorRef = null

  override def preStart(): Unit = {
    super.preStart()
    conf = new SparkConf().setAppName("SPARKNAME").setMaster("local[*]")
    conf.set("spark.cassandra.connection.host", "172.20.78.34")
    ssc = new StreamingContext(conf, Milliseconds(1000))
    kafkaStream = context.actorOf(
      Props( new KafkaBackStreamActor(ssc, kafkaParams, self) ),
      "kappa-backstream-kafka-consumer"
    )
  }

  def receive = {
    case OutputStreamInitialized => {
      ssc.checkpoint("./tmp")
      ssc.start() // currently can not add more dstreams once started
      kafkaStream ! StreamStart
    }
  }
}


