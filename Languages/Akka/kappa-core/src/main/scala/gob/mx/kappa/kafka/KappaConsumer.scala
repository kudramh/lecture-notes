package gob.mx.kappa.kafka

import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConverters._

class KappaConsumer[K, V](config: Properties, kappaTopics: List[String]) {
  val consumer = new KafkaConsumer[K,V](config)
  consumer.subscribe(kappaTopics.asJava)

  def pollRecords() : ConsumerRecords[K, V] = consumer.poll(100)
  def close(): Unit = consumer.close()

}

object KappaConsumer {
  def createConfig(brokers:String, consumerKey:String, deserializerFQCN:String = classOf[StringDeserializer].getName): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("group.id", consumerKey)
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("session.timeout.ms", "30000")
    props.put("key.deserializer",   deserializerFQCN)
    props.put("value.deserializer", deserializerFQCN)
    props
  }
}
