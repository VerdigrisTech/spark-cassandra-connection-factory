# spark-cassandra-connection-factory

Cassandra connection factories for Apache Spark

## Usage

### SBT

In `build.sbt`:

```scala
libraryDependencies += "co.verdigris.spark" %% "spark-cassandra-connection-factory" % "0.3.5"

resolvers += "Verdigris Scala Lib Repository" at "https://s3.amazonaws.com/scala-jars"
```

### Java KeyStore on S3

This library provides a way for Spark users to easily enable 
[client-to-node encryption](https://docs.datastax.com/en/cassandra/3.0/cassandra/configuration/secureSSLClientToNode.html)
in a scalable manner by uploading their Java KeyStore file to S3.

The [Spark Cassandra connector](https://github.com/datastax/spark-cassandra-connector)
currently only provides a [DefaultConnectionFactory](https://github.com/datastax/spark-cassandra-connector/blob/master/spark-cassandra-connector/src/main/scala/com/datastax/spark/connector/cql/CassandraConnectionFactory.scala#L35)
object which requires every Spark nodes to have a copy of Java KeyStore 
file to enable client-to-node encryption.

This may work well if you have the JKS baked into a custom EC2 AMI but
may not scale if your keys are rotated on a regular basis.

#### Uploading to S3

Assuming you have a bucket called `my-tls-bucket` on US East 1 (N.
Virginia) and a Java KeyStore (JKS) file called `my-cluster.jks`:

1. Upload `my-cluster.jks` file to `my-tls-bucket`.
    - On AWS console, click **Upload** to bring up the Upload dialog and
      click **Add files** and click **Next**.
    - On AWS CLI:
      ```sh
      $ aws s3 cp my-cluster.jks s3://my-tls-bucket
      ```
