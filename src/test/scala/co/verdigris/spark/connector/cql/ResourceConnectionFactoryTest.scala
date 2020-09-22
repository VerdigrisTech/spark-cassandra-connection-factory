package co.verdigris.spark.connector.cql

import com.datastax.driver.core.Cluster

class ResourceConnectionFactoryTest extends ConnectionFactorySpec {
  override def beforeAll {
    super.beforeAll

    factory = ResourceConnectionFactory
  }

  describe(".createCluster") {
    it("should return a new Cluster instance") {
      factory.createCluster(cassandraConf) shouldBe a [Cluster]
    }
  }
}
