
package gob.mx.kappa.backport

import akka.actor._
import com.typesafe.config.ConfigFactory

object KappaBackPortApp extends App {

  /** Creates the ActorSystem. */
  val system = ActorSystem("KappaBackPort", ConfigFactory.parseString("akka.remote.netty.tcp.port = 2551"))
  /** The root supervisor and fault tolerance handler of the raw data ingestion nodes. */
  val guardian = system.actorOf(Props(new BackPortGuardian), "backport-node-guardian")
  /** Fuck'em all */
  system.registerOnTermination {
    guardian ! PoisonPill
  }

}

trait BackPortSettings {
  private val config = ConfigFactory.load
  protected val HttpHost = config.getString("kappa.backport.http.host")
  protected val HttpPort = config.getInt("kappa.backport.http.port")
  protected val KafkaBrokers = config.getString("kappa.kafka.brokers")
  protected val KafkaKey = config.getString("kappa.kafka.group.id")
}