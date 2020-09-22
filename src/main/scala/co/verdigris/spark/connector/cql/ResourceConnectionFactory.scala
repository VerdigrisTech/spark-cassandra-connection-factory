package co.verdigris.spark.connector.cql

import com.datastax.driver.core.{Cluster, QueryOptions, SocketOptions, SSLOptions, JdkSSLOptions}
import com.datastax.spark.connector.cql.CassandraConnectorConf.CassandraSSLConf

import java.security._
import javax.net.ssl.{SSLContext, TrustManagerFactory}

class ResourceConnectionFactory extends CassandraConnectionFactory {
  protected def keyStoreFromTrustStoreConf(conf: CassandraSSLConf): Option[KeyStore] = (
    conf.trustStorePath,
    conf.trustStorePassword
  ) match {
    case (None, _) => None
    case (Some(trustStorePath), None) =>
      val trustStoreStream = this.getClass.getClassLoader.getResourceAsStream(trustStorePath)
      val keyStore = KeyStore.getInstance(conf.trustStoreType)
      keyStore.load(trustStoreStream, null)
      Some(keyStore)
    case (Some(trustStorePath), Some(trustStorePassword)) =>
      val trustStoreStream = this.getClass.getClassLoader.getResourceAsStream(trustStorePath)
      val keyStore = KeyStore.getInstance(conf.trustStoreType)
      keyStore.load(trustStoreStream, trustStorePassword.toCharArray)
      Some(keyStore)
  }

  protected def maybeCreateSSLOptions(conf: CassandraSSLConf): Option[JdkSSLOptions] = {
    this.keyStoreFromTrustStoreConf(conf) match {
      case None => None
      case Some(keyStore) => {
        val trustManagerFactory = TrustManagerFactory.getInstance(
          TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        val context = SSLContext.getInstance(conf.protocol)
        context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom())

        Some(JdkSSLOptions
          .builder()
          .withSSLContext(context)
          .withCipherSuites(conf.enabledAlgorithms.toArray)
          .build())
      }
    }
  }
}

object ResourceConnectionFactory extends ResourceConnectionFactory
