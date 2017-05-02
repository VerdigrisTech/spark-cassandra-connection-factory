name := "spark-cassandra-connection-factory"
organization := "co.verdigris.spark"
version := "0.1.0"
scalaVersion := "2.11.10"
crossScalaVersions := Seq("2.10.6", "2.11.10")

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.1"
libraryDependencies += "co.verdigris.ssl" %% "ssllib" % "0.2.1"

resolvers += "Verdigris Scala Lib Repository" at "https://s3.amazonaws.com/scala-jars"
