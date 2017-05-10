# spark-cassandra-connection-factory

Cassandra connection factories for Apache Spark

## Usage

### SBT

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
