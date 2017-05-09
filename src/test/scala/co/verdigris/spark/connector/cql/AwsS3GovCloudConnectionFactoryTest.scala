package co.verdigris.spark.connector.cql

import com.datastax.driver.core.Cluster

class AwsS3GovCloudConnectionFactoryTest extends ConnectionFactorySpec {
  override def beforeAll {
    super.beforeAll

    factory = AwsS3GovCloudConnectionFactory
  }

  describe(".clusterBuilder") {
    it("should return a new Cluster.Builder instance") {
      factory.clusterBuilder(cassandraConf) shouldBe a [Cluster.Builder]
    }
  }

  describe(".createCluster") {
    it("should return a new Cluster instance") {
      factory.createCluster(cassandraConf) shouldBe a [Cluster]
    }
  }
}
