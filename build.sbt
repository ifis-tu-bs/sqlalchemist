name := """SQL-Alchemist-Core"""

version := "1.0"

scalaVersion := "2.11.5"

organization := "de.tu_bs.cs.ifis.sqlgame"

publishTo := Some(Resolver.file("mvn-repository", new File("./mvn-repository/")))

scalacOptions ++= Seq("-encoding", "UTF-8")

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.3.176",
  "org.apache.logging.log4j" % "log4j-core" % "2.2",
  "org.apache.logging.log4j" % "log4j-api" % "2.2",
  "com.typesafe" % "config" % "1.3.0-M3",
  "org.fluttercode.datafactory" % "datafactory" % "0.8",
  "com.novocode" % "junit-interface" % "0.10-M1" % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")