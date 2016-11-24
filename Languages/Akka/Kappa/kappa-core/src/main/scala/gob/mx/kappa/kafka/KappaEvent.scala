package gob.mx.kappa.kafka

object KappaEvent {
  case class KappaMessage[K,V](topic: String, key: K, messages: V*)
  case class RawEvent(topic: String, messages: String*)
}