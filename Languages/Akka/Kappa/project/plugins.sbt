// pulls in: sbt-pgp, sbt-release, sbt-mima-plugin, sbt-dependency-graph, sbt-buildinfo, sbt-sonatype
addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.3.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")
addSbtPlugin("com.scalapenos" % "sbt-prompt" % "1.0.0")

// For Intellij users of older versions:
// This might already be in ~/.sbt.. for Scala users
// addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")