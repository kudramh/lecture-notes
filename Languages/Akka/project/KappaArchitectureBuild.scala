
import sbt._
import sbt.Keys._

object KappaArchitectureBuild extends Build {
  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings,
    aggregate = Seq(core, batchport, batchstream, serving, worksheet, cluster)
  )
  lazy val core = Project(
    id = "core",
    base = file("./kappa-core"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.core)
  )
  lazy val batchport = Project(
    id = "batchport",
    base = file("./kappa-batchport"),
    dependencies = Seq(core),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.batchport)
  )
  lazy val batchstream = Project(
    id = "batchstream",
    base = file("./kappa-batchstream"),
    dependencies = Seq(core),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.batchstream)
  )
  lazy val serving = Project(
    id = "serving",
    base = file("./kappa-serving"),
    dependencies = Seq(core),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.serving)
  )
  lazy val worksheet = Project(
    id = "worksheet",
    base = file("./kappa-worksheet"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.worksheet)
  )
  lazy val cluster = Project(
    id = "cluster",
    base = file("./kappa-cluster"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.cluster)
  )

  lazy val mierdacle = Project(
    id = "mierdacle",
    base = file("./kappa-libs"),
    settings = Seq(
      organization              := "com.oracle",
      name                      := "ojdbc7",
      version                   := "12.1.0.1",
      crossPaths                := false,  //don't add scala version to this artifacts in repo
      publishMavenStyle         := true,
      autoScalaLibrary          := false,  //don't attach scala libs as dependencies
      description               := "Mierdacle Driver",
      packageBin in Compile     := baseDirectory.value / s"${name.value}.jar",
      packageDoc in Compile     := baseDirectory.value / s"${name.value}-javadoc.jar"
    )
  )
}

object Dependencies {
  import Versions._

//  implicit class Exclude(module: ModuleID) {
//    def log4jExclude: ModuleID =
//      module excludeAll(ExclusionRule("log4j"))
//
//    def sparkExclusions: ModuleID =
//      module.log4jExclude.exclude("com.google.guava", "guava")
////        .exclude("org.apache.spark", "spark-core")
////        .exclude("org.slf4j", "slf4j-log4j12")
//
//    def kafkaExclusions: ModuleID =
//      module.log4jExclude.excludeAll(ExclusionRule("org.slf4j"))
//      .exclude("com.sun.jmx", "jmxri")
//      .exclude("com.sun.jdmk", "jmxtools")
//      .exclude("net.sf.jopt-simple", "jopt-simple")
//  }

  object Compile {
    val akkaActor         = "com.typesafe.akka"      %% "akka-actor"                         % Akka
    val akkaStream        = "com.typesafe.akka"      %% "akka-stream"                        % Akka
    val akkaHttpCore      = "com.typesafe.akka"      %% "akka-http-core"                     % Akka
    val akkaHttpExp       = "com.typesafe.akka"      %% "akka-http-experimental"             % Akka
    val akkaRemote        = "com.typesafe.akka"      %% "akka-remote"                        % Akka
    val akkaCluster       = "com.typesafe.akka"      %% "akka-cluster"                       % Akka
    val akkaSlf4j         = "com.typesafe.akka"      %% "akka-slf4j"                         % Akka
    val slf4jApi          = "org.slf4j"              %  "slf4j-api"                          % Slf4j
    val logback           = "ch.qos.logback"         %  "logback-classic"                    % Logback
    val kafkaCore         = "org.apache.kafka"       %% "kafka"                              % Kafka
    val jodaTime          = "joda-time"              %  "joda-time"                          % JodaTime % "compile;runtime"
    val jodaConvert       = "org.joda"               %  "joda-convert"                       % JodaConvert % "compile;runtime"
    val json4sCore        = "org.json4s"             %% "json4s-core"                        % Json4s
    val json4sJackson     = "org.json4s"             %% "json4s-jackson"                     % Json4s
    val json4sNative      = "org.json4s"             %% "json4s-native"                      % Json4s
    val sparkML           = "org.apache.spark"       %% "spark-mllib"                        % Spark
    val sparkCatalyst     = "org.apache.spark"       %% "spark-catalyst"                     % Spark
    val sparkKafkaStream  = "org.apache.spark"       %% "spark-streaming-kafka"              % Spark
    val sparkSQL          = "org.apache.spark"       %% "spark-sql"                          % Spark
    val sparkCassandra    = "com.datastax.spark"     %% "spark-cassandra-connector"          % SparkCassandra
    val cassandraDriver   = "com.datastax.cassandra" %  "cassandra-driver-core"              % CassandraDriver
    val asyncHttpClient   = "com.ning"               % "async-http-client"                   % AsyncHttpClient
    val jsoup             = "org.jsoup"              % "jsoup"                               % Jsoup
    val netty             = "io.netty"               % "netty"                               % Netty force()
  }
  object Test {
    val akkaTestKit     = "com.typesafe.akka"     %% "akka-testkit"                       % Akka      % "test,it"
  }

  import Compile._

  /** Libraries deps */
  val akka      = Seq(akkaStream, akkaHttpCore, akkaHttpExp, akkaActor, akkaCluster, akkaRemote, akkaSlf4j)
  val logging   = Seq(logback, slf4jApi)
  val kafka     = Seq(kafkaCore)
  val json      = Seq(json4sCore, json4sJackson, json4sNative)
  val spark     = Seq(sparkCatalyst, sparkML, sparkKafkaStream, sparkCassandra, sparkSQL)
  val cassandra = Seq(cassandraDriver)
  val time      = Seq(jodaConvert, jodaTime)
  val common    = akka ++ logging ++ time
  val web       = Seq(asyncHttpClient,jsoup,netty)

  /** Module deps */
  val core = common
  val batchport = common
  val batchstream = common ++ spark
  val serving = common ++ spark ++ cassandra
  val worksheet = spark ++ cassandra ++ time
  val cluster = common ++ kafka ++ web

}
