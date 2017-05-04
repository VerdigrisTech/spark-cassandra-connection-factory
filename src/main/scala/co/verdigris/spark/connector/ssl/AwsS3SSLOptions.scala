package co.verdigris.spark.connector.ssl

import co.verdigris.ssl.S3JKSSSLOptions
import com.amazonaws.regions.Region
import com.datastax.spark.connector.cql.CassandraConnectorConf.CassandraSSLConf

class AwsS3SSLOptions(
      sslConf: CassandraSSLConf,
      awsRegion: Option[String] = None)
    extends S3JKSSSLOptions(
      sslConf.trustStorePath,
      sslConf.trustStorePassword,
      Some(sslConf.trustStoreType),
      sslConf.keyStorePath,
      sslConf.keyStorePassword,
      Some(sslConf.keyStoreType),
      sslConf.enabledAlgorithms,
      awsRegion)

/**
  * AwsS3SSLOptions object contains the builder function to facilitate instantiate the AwsS3SSLOptions class.
  */
object AwsS3SSLOptions {
  class Builder {
    protected var cassandraSSLConf: Option[CassandraSSLConf] = None
    protected var awsRegion: Option[String] = None

    /** Set the AWS S3 region.
      *
      * @param awsRegion S3 bucket region.
      * @return this builder.
      */
    def withAwsRegion(awsRegion: Option[String]): Builder = {
      this.awsRegion = awsRegion
      this
    }

    /** Set the AWS S3 region.
      *
      * @param awsRegion S3 bucket region.
      * @return this builder.
      */
    def withAwsRegion(awsRegion: String): Builder = {
      this.withAwsRegion(Some(awsRegion))
    }

    /** Set the AWS S3 region.
      *
      * @param awsRegion S3 bucket region.
      * @return this builder.
      */
    def withAwsRegion(awsRegion: Region): Builder = {
      this.awsRegion = Some(awsRegion.getName)
      this
    }

    /** Set the cipher suites to use.
      * <p/>
      * If this method isn't called, the default is to present all the eligible client ciphers to the server.
      *
      * @param cipherSuites set of cipher suites to use.
      * @return this builder.
      */
    def withCipherSuites(cipherSuites: Set[String]): Builder = {
      val oldConf = this.cassandraSSLConf
        .getOrElse(new CassandraSSLConf(enabledAlgorithms = cipherSuites))

      this.cassandraSSLConf = Some(
        new CassandraSSLConf(
          oldConf.enabled,
          oldConf.trustStorePath,
          oldConf.trustStorePassword,
          oldConf.trustStoreType,
          oldConf.protocol,
          cipherSuites,
          oldConf.clientAuthEnabled,
          oldConf.keyStorePath,
          oldConf.keyStorePassword,
          oldConf.keyStoreType))

      this
    }

    /** Set the Cassandra SSL configuration.
      * <p/>
      * If this method isn't called, it will
      *
      * @param conf
      * @return
      */
    def withSSLConf(conf: CassandraSSLConf): Builder = {
      this.cassandraSSLConf = Some(conf)
      this
    }

    /** This function is used to instantiate an [[AwsS3SSLOptions]]. It uses the AWS region, Cassandra SSL
      * configurations, and cipher suites that were passed in to build the object.
      *
      * @return new instance of AwsS3SSLOptions
      */
    def build(): AwsS3SSLOptions =
      new AwsS3SSLOptions(
        this.cassandraSSLConf.getOrElse(new CassandraSSLConf()),
        this.awsRegion)
  }

  def builder(): Builder = new Builder
}
