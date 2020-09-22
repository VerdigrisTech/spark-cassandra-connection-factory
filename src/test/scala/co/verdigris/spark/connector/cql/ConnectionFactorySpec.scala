package co.verdigris.spark.connector.cql

import java.net.InetAddress

import com.datastax.spark.connector.cql.CassandraConnectorConf
import org.scalatest.{BeforeAndAfterEach, Matchers}

trait ConnectionFactorySpec extends SparkContextUnitSpec with Matchers with BeforeAndAfterEach {
  var cassandraConf: CassandraConnectorConf = _
  var sslConf: CassandraConnectorConf.CassandraSSLConf = _
  var factory: CassandraConnectionFactory = _

  override def beforeEach {
    sslConf = CassandraConnectorConf.CassandraSSLConf(enabled = true, Some("s3://bucket/key.jks"), Some("password"))
    cassandraConf = CassandraConnectorConf(Set(InetAddress.getByName("127.0.0.1")), cassandraSSLConf = sslConf)
  }
}
