package co.verdigris.spark.connector.cql

import com.datastax.driver.core.{Cluster, QueryOptions, SocketOptions, SSLOptions}
import com.datastax.driver.core.SSLOptions
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy
import com.datastax.spark.connector.cql.{
  CassandraConnectionFactory => CCF,
  CassandraConnectorConf,
  LocalNodeFirstLoadBalancingPolicy,
  MultipleRetryPolicy
}
import com.datastax.spark.connector.cql.CassandraConnectorConf.CassandraSSLConf

abstract class CassandraConnectionFactory extends CCF {
  /** Returns the Cluster.Builder object used to setup Cluster instance. */
  def clusterBuilder(conf: CassandraConnectorConf): Cluster.Builder = {
    val options = new SocketOptions()
      .setConnectTimeoutMillis(conf.connectTimeoutMillis)
      .setReadTimeoutMillis(conf.readTimeoutMillis)

    val builder = Cluster.builder()
      .addContactPoints(conf.hosts.toSeq: _*)
      .withPort(conf.port)
      .withRetryPolicy(
        new MultipleRetryPolicy(conf.queryRetryCount))
      .withReconnectionPolicy(
        new ExponentialReconnectionPolicy(conf.minReconnectionDelayMillis, conf.maxReconnectionDelayMillis))
      .withLoadBalancingPolicy(
        new LocalNodeFirstLoadBalancingPolicy(conf.hosts, conf.localDC))
      .withAuthProvider(conf.authConf.authProvider)
      .withSocketOptions(options)
      .withCompression(conf.compression)
      .withQueryOptions(
        new QueryOptions()
          .setRefreshNodeIntervalMillis(0)
          .setRefreshNodeListIntervalMillis(0)
          .setRefreshSchemaIntervalMillis(0))

    if (conf.cassandraSSLConf.enabled) {
      maybeCreateSSLOptions(conf.cassandraSSLConf) match {
        case Some(sslOptions) ⇒ builder.withSSL(sslOptions)
        case None ⇒ builder.withSSL()
      }
    } else {
      builder
    }
  }

  protected def maybeCreateSSLOptions(conf: CassandraSSLConf): Option[SSLOptions]

  override def createCluster(conf: CassandraConnectorConf): Cluster = clusterBuilder(conf).build()
}
