package co.verdigris.spark.connector.cql

import com.datastax.driver.core.Cluster

class AwsS3CACentral1ConnectionFactoryTest extends ConnectionFactorySpec {
  override def beforeAll {
    super.beforeAll

    factory = AwsS3CACentral1ConnectionFactory
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
