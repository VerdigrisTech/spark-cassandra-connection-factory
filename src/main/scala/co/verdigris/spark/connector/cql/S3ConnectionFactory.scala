package co.verdigris.spark.connector.cql

import co.verdigris.spark.connector.ssl.AwsS3SSLOptions
import com.datastax.driver.core.SSLOptions
import com.datastax.spark.connector.cql.CassandraConnectorConf.CassandraSSLConf

class S3ConnectionFactory extends CassandraConnectionFactory {
  protected def maybeCreateSSLOptions(conf: CassandraSSLConf): Option[SSLOptions] = {
    if (conf.enabled) {
      Some(
        AwsS3SSLOptions.builder()
          .withSSLConf(conf)
          .build()
      )
    } else {
      None
    }
  }
}

object S3ConnectionFactory extends S3ConnectionFactory
