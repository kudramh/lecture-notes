
import scala.language.postfixOps

import sbt._
import sbt.Keys._
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._

object Settings extends Build {

  lazy val buildSettings = Seq(
    name := "Kappa Lambda Architecture",
    normalizedName := "KappaLambdaArchitecture",
    organization := "gob.mx.kappa",
    organizationHomepage := Some(url("http://www.gob.mx")),
    scalaVersion := Versions.Scala,
    homepage := Some(url("https://github.com/GOBMX/kappa.lambda")),
    licenses := Seq(("gob.mx, beta version", url("http://www.gob.mx/terminos"))),
    promptTheme := ScalapenosTheme
  )
  override lazy val settings = super.settings ++ buildSettings

  val parentSettings = buildSettings ++ Seq(
    publishArtifact := false,
    publish := {}
  )

  lazy val defaultSettings = testSettings ++ graphSettings ++ Seq(
    autoCompilerPlugins := true,
    // removed "-Xfatal-warnings" as temporary workaround for log4j fatal error.
    scalacOptions ++= Seq("-encoding", "UTF-8", s"-target:jvm-${Versions.JDK}", "-feature", "-language:_", "-deprecation", "-unchecked", "-Xlint"),
    javacOptions in Compile ++= Seq("-encoding", "UTF-8", "-source", Versions.JDK, "-target", Versions.JDK, "-Xlint:deprecation", "-Xlint:unchecked"),
    run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)),
    ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet,
    parallelExecution in ThisBuild := false,
    parallelExecution in Global := false/*,
    ivyXML := <dependencies>
      <exclude org="org.slf4j" module="slf4j-log4j12"/>
    </dependencies>*/
  )

  val tests = inConfig(Test)(Defaults.testTasks) ++ inConfig(IntegrationTest)(Defaults.itSettings)

  val testOptionSettings = Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
  )

  lazy val testSettings = tests ++ Seq(
    parallelExecution in Test := false,
    parallelExecution in IntegrationTest := false,
    testOptions in Test ++= testOptionSettings,
    testOptions in IntegrationTest ++= testOptionSettings,
    baseDirectory in Test := baseDirectory.value.getParentFile(),
    fork in Test := true,
    fork in IntegrationTest := true,
    (compile in IntegrationTest) <<= (compile in Test, compile in IntegrationTest) map { (_, c) => c },
    managedClasspath in IntegrationTest <<= Classpaths.concat(managedClasspath in IntegrationTest, exportedProducts in Test)
  )

}
