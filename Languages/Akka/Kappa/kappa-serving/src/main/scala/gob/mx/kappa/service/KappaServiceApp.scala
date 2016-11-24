package gob.mx.kappa.service

import akka.actor.{ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory

object KappaServiceApp extends App {

  /** Creates the ActorSystem. */
  val system = ActorSystem("KappaBackPort", ConfigFactory.parseString("akka.remote.netty.tcp.port = 2557"))
  /** The root supervisor and fault tolerance handler of the raw data ingestion nodes. */
  val guardian = system.actorOf(Props(new ServiceGuardian), "service-node-guardian")
  /** Fuck'em all */
  system.registerOnTermination {
    guardian ! PoisonPill
  }

}
