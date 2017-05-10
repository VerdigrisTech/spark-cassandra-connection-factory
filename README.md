# spark-cassandra-connection-factory

Cassandra connection factories for Apache Spark

## Usage

### Adding `spark-cassandra-connection-factory` as dependency in your project

This library is built against version `2.0.0-M3` of `spark-cassandra-connector`
and will not be compatible with earlier versions.

#### SBT

In `build.sbt`:

```scala
libraryDependencies += "co.verdigris.spark" %% "spark-cassandra-connection-factory" % "0.3.5"

resolvers += "Verdigris Scala Lib Repository" at "https://s3.amazonaws.com/scala-jars"
```

## Java KeyStore on S3

This library provides a way for Spark users to easily enable
[client-to-node encryption](https://docs.datastax.com/en/cassandra/3.0/cassandra/configuration/secureSSLClientToNode.html)
in a scalable manner by uploading their Java KeyStore file to S3.

The [Spark Cassandra connector](https://github.com/datastax/spark-cassandra-connector)
currently only provides a [DefaultConnectionFactory](https://github.com/datastax/spark-cassandra-connector/blob/master/spark-cassandra-connector/src/main/scala/com/datastax/spark/connector/cql/CassandraConnectionFactory.scala#L35)
object which requires every Spark nodes to have a copy of Java KeyStore
file to enable client-to-node encryption.

This may work well if you have the JKS baked into a custom EC2 AMI but
may not scale if your keys are rotated on a regular basis.

### Uploading to S3

Assuming you have a bucket called `my-tls-bucket` on US East 1 (N.
Virginia) and a Java KeyStore (JKS) file called `my-cluster.jks`:

#### Uploading through AWS Console UI

1. Upload `my-cluster.jks` file to `my-tls-bucket` by clicking on **Upload** to
   bring up the Upload dialog and click **Add files** to select your file and
   click **Next**.

2. Set permissions so that authenticated AWS user has a read-only access.
   > ##### IMPORTANT
   > For security reasons, make sure to disable any read or write access for
   > group: `Everyone`!

3. Click **Next**.

4. In the **Set properties** step, set the storage class to `Standard`. It is
   also **_VERY HIGHLY_** recommended to enable [server-side encryption (SSE)](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingServerSideEncryption.html)
   via [AWS KMS](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingKMSEncryption.html).
   This has an added security benefit of being able to associate an IAM role to
   the private key through an IAM policy; all other IAM users or roles not
   associated with the KMS key will not have access to the S3 object encrypted
   with that key.

5. Click **Next** to review your configuration one last time and click
   **Upload**.

#### Uploading through AWS CLI

```sh
$ aws s3 cp /path/to/my-cluster.jks s3://my-tls-bucket/remote/path/my-cluster.jks --acl authenticated-read --sse aws:kms --sse-kms-key-id $MY_KMS_ARN
```

### Setting configurations for your Spark context

Continuing from [previous section](#uploading-to-s3), we will assume your JKS
file is hosted on `my-tls-bucket` S3 bucket on US East 1 (N. Virginia).

> ##### NOTE
> If you have SSE-KMS enabled and are running this example on an AWS EC2
> instance, be sure the IAM role associated with the EC2 instance has access to
> the KMS private key.

#### Basic example

```scala
val conf = new SparkConf()
  .setMaster("local[*]")
  .setAppName("JKS on S3 Example")
  .set("spark.cassandra.connection.host", "node-1.mycluster.example.com")
  .set("spark.cassandra.auth.username", "rickastley")
  .set("spark.cassandra.auth.password", "nevergonnagiveyouup")
  .set("spark.cassandra.connection.factory", "co.verdigris.spark.connector.cql.AwsS3USEast1ConnectionFactory")
  .set("spark.cassandra.connection.ssl.enabled", "true")
  .set("spark.cassandra.connection.ssl.trustStore.path", "s3://my-tls-bucket/my-cluster.jks")
  .set("spark.cassandra.connection.ssl.trustStore.password", "nevergonnaletyoudown")

val sc = SparkContext.getOrCreate(conf)
```

#### Multiple clusters

If you have multiple clusters with different JKS files or even different
client-to-node encryption settings altogether, you can scope your
Spark configuration on a per-connector-basis:

```scala
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql._

val cluster1 = CassandraConnector(sc.getConf
    .set("spark.cassandra.connection.host", "node-1.mycluster.example.com")
    .set("spark.cassandra.auth.username", "rickastley")
    .set("spark.cassandra.auth.password", "nevergonnagiveyouup")
    .set("spark.cassandra.connection.factory", "co.verdigris.spark.connector.cql.AwsS3USEast1ConnectionFactory")
    .set("spark.cassandra.connection.ssl.enabled", "true")
    .set("spark.cassandra.connection.ssl.trustStore.path", "s3://my-tls-bucket/my-cluster.jks")
    .set("spark.cassandra.connection.ssl.trustStore.password", "nevergonnaletyoudown"))

val cluster2 = CassandraConnector(sc.getConf
    .set("spark.cassandra.connection.host", "node-1.othercluster.example.com")
    .set("spark.cassandra.auth.username", "rickastley")
    .set("spark.cassandra.auth.password", "nevergonnagiveyouup")
    .set("spark.cassandra.connection.factory", "co.verdigris.spark.connector.cql.AwsS3USEast1ConnectionFactory")
    .set("spark.cassandra.connection.ssl.enabled", "true")
    .set("spark.cassandra.connection.ssl.trustStore.path", "s3://my-tls-bucket/other-cluster.jks")
    .set("spark.cassandra.connection.ssl.trustStore.password", "nevergonnatellalie"))

val localDevCluster = CassandraConnector(sc.getConf
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.connection.ssl.enabled", "false"))

val lyricsRdd = {
    implicit val c = cluster1
    sc.cassandraTable("some_keyspace", "lyrics_by_artist")
        .where("artist = ?", "Rick Astley")
}

{
    implicit val c = cluster2
    lyricsRdd.saveToCassandra("rick_astley", "my_lyrics")
}
```

## Known Issues

### Missing support for client auth

Because the `2.0.0-M3` version of `spark-cassandra-connector` library does not
have support for client auth, this library currently does not support client
auth at the moment. It will be added once we build against `2.0.1`.
