import com.amazonaws.services.s3.model.Region

name := "spark-cassandra-connection-factory"
organization := "co.verdigris.spark"
version := "0.4.0"
scalaVersion := "2.11.12"
crossScalaVersions := Seq("2.11.12", "2.12.12")

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.2" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.2" % "provided"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.3.2" % "provided"
libraryDependencies += "co.verdigris.ssl" %% "ssllib" % "1.1.2"
libraryDependencies += scalaTest % Test

s3region := Region.US_Standard
s3overwrite := true
publishTo := Some(s3resolver.value("Verdigris Scala Libs", s3("scala-jars")))

resolvers += "Verdigris Scala Lib Repository" at "https://s3.amazonaws.com/scala-jars"
