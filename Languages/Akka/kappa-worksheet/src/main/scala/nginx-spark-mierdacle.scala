

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.jdbc.JdbcDialect
import org.joda.time.format.DateTimeFormat
//import scala.io.Source

case class LogData(
  remote_addr:String,
  year:String,
  month:String,
  day:String,
  hour:String,
  minute:String,
  method:String,
  request:String,
  full_request:String,
  protocol:String,
  status:String,
  body_bytes_sent:String,
  http_referer:String,
  http_user_agent:String
)

object LogData {
  private val groups = List(
    ("([\\d|.]*)",    "remote_addr"),
    ("([^ ]*)",       "ignore1"),
    ("([^ ]*)",       "ignore2"),
    ("\\[([^]]*)\\]", "time_local"),
    ("\"([^\"]*)\"",  "request"),
    ("([^ ]*)",       "status"),
    ("([^ ]*)",       "body_bytes_sent"),
    ("\"([^\"]*)\"",  "http_referer"),
    ("\"([^\"]*)\"",  "http_user_agent")
  )
  def cleanShit(str:String, chars:Array[String]):String = {
    def loop(astr:String, item:Int): String ={
      if(item>chars.length-1)
        astr
      else {
        loop( if(astr.contains(chars(item))) astr.substring(0,astr.indexOf(chars(item)))
              else astr,
              item+1)
      }
    }
    loop(str, 0)
  }
  protected val pattern = (groups.map(_._1) mkString " ").r
  private   val dateFormatter = DateTimeFormat.forPattern("dd/MMM/yyyy:HH:mm:ss Z")
  private   val shitted = Array("/", "?")
  def apply(data:String): LogData = {
    try {
      val pattern(remote_addr, ignore, remote_user, time_local, request,
                  status, body_bytes_sent, http_referer, http_user_agent) = data
      val eventDate = dateFormatter.parseDateTime(time_local);
      val requestParts = request.split(" ")
      val requested = cleanShit(requestParts(1).replaceFirst("/",""), shitted)
      val referer   = cleanShit(http_referer.replace("http://",""),   shitted)
      LogData(
        remote_addr.trim,
        eventDate.getYear.toString,
        eventDate.getMonthOfYear.toString,
        eventDate.getDayOfMonth.toString,
        eventDate.getHourOfDay.toString,
        eventDate.getMinuteOfHour.toString,
        requestParts(0).trim,
        requested.trim,
        "", //requestParts(1).trim,
        requestParts(2).trim,
        status.trim,
        body_bytes_sent.trim,
        referer,
        http_user_agent.trim
      )
    }
    catch {
      case ex:Exception  =>
        println(data, ex)
        LogData("", "", "", "", "","", "", "", "", "","","","","")
    }
  }
}

import org.apache.spark.sql.jdbc.{JdbcDialects, JdbcType, JdbcDialect}
import org.apache.spark.sql.types._

object NginxSparkMierdacle {

  def main(args: Array[String]): Unit ={

    val OracleDialect = new JdbcDialect {
      override def canHandle(url: String): Boolean = url.startsWith("jdbc:oracle") || url.contains("oracle")
      override def getJDBCType(dt: DataType): Option[JdbcType] = dt match {
        case StringType => Some(JdbcType("VARCHAR2(2000)", java.sql.Types.VARCHAR))
        case BooleanType => Some(JdbcType("NUMBER(1)", java.sql.Types.NUMERIC))
        case IntegerType => Some(JdbcType("NUMBER(10)", java.sql.Types.NUMERIC))
        case LongType => Some(JdbcType("NUMBER(19)", java.sql.Types.NUMERIC))
        case DoubleType => Some(JdbcType("NUMBER(19,4)", java.sql.Types.NUMERIC))
        case FloatType => Some(JdbcType("NUMBER(19,4)", java.sql.Types.NUMERIC))
        case ShortType => Some(JdbcType("NUMBER(5)", java.sql.Types.NUMERIC))
        case ByteType => Some(JdbcType("NUMBER(3)", java.sql.Types.NUMERIC))
        case BinaryType => Some(JdbcType("BLOB", java.sql.Types.BLOB))
        case TimestampType => Some(JdbcType("DATE", java.sql.Types.DATE))
        case DateType => Some(JdbcType("DATE", java.sql.Types.DATE))
        //      case DecimalType.Fixed(precision, scale) => Some(JdbcType("NUMBER(" + precision + "," + scale + ")", java.sql.Types.NUMERIC))
        case DecimalType.Unlimited => Some(JdbcType("NUMBER(38,4)", java.sql.Types.NUMERIC))
        case _ => None
      }
    }
    JdbcDialects.registerDialect(OracleDialect)
    val cnxShit = new java.util.Properties
    cnxShit.setProperty("driver","oracle.jdbc.OracleDriver")
    cnxShit.setProperty("user","OWVUNDWH")
    cnxShit.setProperty("password","OWVUNDWH")

    val filename = "/Users/marduk/Downloads/nginx/nginx.log"
    val conf = new SparkConf().setAppName("NginxLogSparked").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._
    val logDataFrame = sc.textFile(filename).map(LogData(_)).toDF()
    logDataFrame.printSchema()

    logDataFrame.write.mode(SaveMode.Append).jdbc("jdbc:oracle:thin:@//10.15.109.5:1521/PVUNDWH", "NGINXSPARK", cnxShit)

    //    logDataFrame.groupBy("request").count().orderBy(desc("count")).show()
    //    logDataFrame.groupBy("status").count().orderBy(desc("count")).show()
    //    logDataFrame.groupBy("status").agg(sum("body_bytes_sent")/(1024*1024)).show()
    //    println(logDataFrame.count())
    //    logDataFrame.show()

  }

}


