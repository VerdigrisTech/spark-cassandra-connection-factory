import com.amazonaws.services.s3.model.Region

name := "spark-cassandra-connection-factory"
organization := "co.verdigris.spark"
version := "0.3.1"
scalaVersion := "2.11.10"
crossScalaVersions := Seq("2.10.6", "2.11.10")

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.2" % "provided"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.1" % "provided"
libraryDependencies += "co.verdigris.ssl" %% "ssllib" % "1.1.0"

s3region := Region.US_Standard
s3overwrite := true
publishTo := Some(s3resolver.value("Verdigris Scala Libs", s3("scala-jars")))

resolvers += "Verdigris Scala Lib Repository" at "https://s3.amazonaws.com/scala-jars"
