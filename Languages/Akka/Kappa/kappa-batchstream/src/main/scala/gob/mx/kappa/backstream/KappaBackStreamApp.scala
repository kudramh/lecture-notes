package gob.mx.kappa.backstream

import akka.actor._
import com.typesafe.config.ConfigFactory
import gob.mx.kappa.backstream.StreamMessages.StreamStart
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;


object KappaBackStreamApp extends App {

  LoggerFactory.getLogger("org.spark-project.jetty").asInstanceOf[Logger].setLevel(Level.OFF)
  LoggerFactory.getLogger("org.apache.zookeeper").asInstanceOf[Logger].setLevel(Level.OFF)
  LoggerFactory.getLogger("org.apache.spark").asInstanceOf[Logger].setLevel(Level.OFF)

  /** Creates the ActorSystem. */
  val system = ActorSystem("KappaBackStream", ConfigFactory.parseString("akka.remote.netty.tcp.port = 2555"))
  /** The root supervisor and fault tolerance handler of the raw data ingestion nodes. */
  val guardian = system.actorOf(Props(new BackStreamGuardian), "backstrean-node-guardian")

  /** Fuck'em all */
  system.registerOnTermination {
    guardian ! PoisonPill
  }

}

object StreamMessages {
  case class OutputStreamInitialized()
  case class StreamStart()
  case class StreamStop()
}

trait BackStreamSettings {
  private val config = ConfigFactory.load
  protected val kafkaParams = Map[String, String](
    "zookeeper.connect" -> "127.0.0.1:2181",
    "group.id" -> "kappa.backport"
  )

}