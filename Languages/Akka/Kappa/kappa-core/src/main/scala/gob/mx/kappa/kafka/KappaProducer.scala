package gob.mx.kappa.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.clients.producer.ProducerRecord


/** Simple producer using string encoder and default partitioner. */
class KappaProducer[K, V](config: Properties) {
  import KappaEvent._
  private val producer = new KafkaProducer[K, V](config)

  /** Sends the data, partitioned by key to the topic. */
  def send(e: KappaMessage[K,V]): Unit = {
    e.messages.foreach( msg =>  send(e.topic, e.key, msg) )
  }
  /** Sends a single message. */
  private def send(topic : String, key : K, msg : V): Unit = {
    val message = new ProducerRecord[K,V](topic, key, msg)
    producer.send(message)
  }

  def close(): Unit = producer.close()

}

object KappaProducer {
  def createConfig(brokers:String, producerKey:String, serializerFQCN:String = classOf[StringSerializer].getName): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("group.id", producerKey)
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("session.timeout.ms", "30000")
    props.put("key.serializer",     serializerFQCN)
    props.put("value.serializer",   serializerFQCN)
    props
  }
}