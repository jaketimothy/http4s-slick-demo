// build.sbt

lazy val commonSettings = Seq(
  organization := "com.jaketimothy",
  version := "0.1.0",
  scalaVersion := "2.11.8"
  )

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
	name := "http4s-demo",
	libraryDependencies ++= Seq(
	  "org.http4s" % "http4s-core_2.11" % "0.13.2",
    "org.http4s" % "http4s-dsl_2.11" % "0.13.2",
    "org.http4s" % "http4s-server_2.11" % "0.13.2",
    "org.http4s" % "http4s-blaze-server_2.11" % "0.13.2",
    "com.typesafe.slick" % "slick_2.11" % "3.1.1",
    "com.h2database" % "h2" % "1.4.191"
	  )
	)