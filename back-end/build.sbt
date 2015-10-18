name := """The SQL Alchemist"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    javaJdbc,
    cache,
    javaWs,
    evolutions,
    "org.mindrot" % "jbcrypt" % "0.3m",
    "com.typesafe.play" %% "play-mailer" % "3.0.1",
    "mysql" % "mysql-connector-java" % "5.1.35",
    "org.apache.logging.log4j" % "log4j-api" % "2.3",
    "org.apache.logging.log4j" % "log4j-core" % "2.3",
    "org.fluttercode.datafactory" % "datafactory" % "0.8"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

javacOptions += "-Xlint:deprecation"