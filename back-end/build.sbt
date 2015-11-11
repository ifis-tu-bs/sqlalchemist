name := """SQL-Alchemist"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, JDebPackaging)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    javaJdbc,
    cache,
    javaWs,
    evolutions,
    "org.mindrot" % "jbcrypt" % "0.3m",
    "com.typesafe.play" %% "play-mailer" % "3.0.1",
    "mysql" % "mysql-connector-java" % "5.1.35"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

javacOptions += "-Xlint:deprecation"



/// packageSummary
import com.typesafe.sbt.packager.archetypes.ServerLoader.{SystemV, Upstart}

name in Debian := "sqlalchemist"

maintainer in Linux := "Fabio Mazzone<fabio.mazzone@me.com>"

packageSummary in Linux := "SQL Alchemist"

debianPackageDependencies in Debian ++= Seq("nginx", "mysql-server-5.6")

serverLoading in Debian := Upstart

daemonUser in Linux := normalizedName.value         // user which will execute the application

daemonGroup in Linux := (daemonUser in Linux).value // group which will execute the application
