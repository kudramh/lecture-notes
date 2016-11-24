package gob.mx.kappa.service.actors.utils

import akka.http.scaladsl.server.Directives._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.SparkConf

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object HttpRoutes {

    val routes =
      get{
        path("api" / "raw" / "data") {
          val conf = new SparkConf(true)
            .set("spark.cassandra.connection.host", "172.20.78.34")
            .setMaster("local[*]")
            .setAppName("app2")
          val conn = CassandraConnector(conf)
          var resultSet = new ListBuffer[String]()
          conn.withSessionDo { session =>
            val result = session.execute("SELECT * FROM kappa_batch.raw_data").all().asScala
            result.foreach( resultSet += _.toString)
          }
          complete(resultSet.mkString("\n"))
        }
      }
}
