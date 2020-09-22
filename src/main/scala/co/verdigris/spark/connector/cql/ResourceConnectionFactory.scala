package co.verdigris.spark.connector.cql

import com.datastax.driver.core.{Cluster, QueryOptions, SocketOptions, SSLOptions, JdkSSLOptions}
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy
import com.datastax.spark.connector.cql._

import java.security._
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}


class ResourceConnectionFactory extends CassandraConnectionFactory {
  private def clusterBuilder(conf: CassandraConnectorConf): Cluster.Builder = {
    val socketOptions = new SocketOptions()
      .setConnectTimeoutMillis(conf.connectTimeoutMillis)
      .setReadTimeoutMillis(conf.readTimeoutMillis)

    val builder = Cluster.builder()
      .addContactPoints(conf.hosts.toSeq: _*)
      .withPort(conf.port)
      .withRetryPolicy(
        new MultipleRetryPolicy(conf.queryRetryCount))
      .withReconnectionPolicy(
        new ExponentialReconnectionPolicy(
          conf.minReconnectionDelayMillis,
          conf.maxReconnectionDelayMillis))
      .withLoadBalancingPolicy(
        new LocalNodeFirstLoadBalancingPolicy(conf.hosts, conf.localDC))
      .withAuthProvider(conf.authConf.authProvider)
      .withSocketOptions(socketOptions)
      .withCompression(conf.compression)
      .withQueryOptions(
        new QueryOptions()
          .setRefreshNodeIntervalMillis(0)
          .setRefreshNodeListIntervalMillis(0)
          .setRefreshSchemaIntervalMillis(0))

    if (conf.cassandraSSLConf.enabled) {
      maybeCreateSSLOptions(conf.cassandraSSLConf) match {
        case Some(sslOptions) => builder.withSSL(sslOptions)
        case None => builder.withSSL()
      }
    } else {
      builder
    }
  }

  protected def keyStoreFromTrustStoreConf(
    conf: CassandraConnectorConf.CassandraSSLConf
  ): Option[KeyStore] = (conf.trustStorePath, conf.trustStorePassword) match {
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

  protected def maybeCreateSSLOptions(
    conf: CassandraConnectorConf.CassandraSSLConf
  ): Option[JdkSSLOptions] = {
    this.keyStoreFromTrustStoreConf(conf) match {
      case None => None
      case Some(keyStore) => {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
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

  override def createCluster(conf: CassandraConnectorConf): Cluster = clusterBuilder(conf).build()
}
