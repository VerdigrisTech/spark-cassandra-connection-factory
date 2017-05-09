package co.verdigris.spark.connector.ssl

import com.amazonaws.regions.{Region, Regions}
import com.datastax.spark.connector.cql.CassandraConnectorConf.CassandraSSLConf
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}

class AwsS3SSLOptionsTest extends FunSpec with Matchers with BeforeAndAfter {
  var builder: AwsS3SSLOptions.Builder = _

  before {
    this.builder = AwsS3SSLOptions.builder()
  }

  describe("Builder") {
    describe("#withAwsRegion") {
      it("should set the correct region") {
        builder.withAwsRegion(Some("us-east-1"))
          .build()
          .awsRegion shouldBe Some("us-east-1")
        builder.withAwsRegion("us-east-1")
          .build()
          .awsRegion shouldBe Some("us-east-1")
        builder.withAwsRegion(Region.getRegion(Regions.US_EAST_1))
          .build()
          .awsRegion shouldBe Some("us-east-1")
      }

      it("should return this instance") {
        builder.withAwsRegion(Some("us-east-1")) shouldBe builder
        builder.withAwsRegion("us-east-1") shouldBe builder
        builder.withAwsRegion(Region.getRegion(Regions.US_EAST_1)) shouldBe builder
      }
    }

    describe("#withCiphterSuites") {
      it("should set the cipher suites") {
        builder.withCipherSuites(Set("TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA"))
          .build()
          .cipherSuites should contain allOf("TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA")
      }

      it("should override cipher suites from CassandraSSLConf instance") {
        val cipherSuites = builder.withSSLConf(
          CassandraSSLConf(
            enabled = true,
            Some("s3://bucket/key.jks"),
            Some("pwd"),
            "JKS",
            "TLS",
            Set("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384")))
          .withCipherSuites(Set("TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA"))
          .build()
          .cipherSuites

        cipherSuites should contain noneOf(
          "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
          "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384")
        cipherSuites should contain allOf("TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA")
      }

      it("should return this instance") {
        builder.withCipherSuites(Set("TLS_RSA_WITH_AES_256_CBC_SHA")) shouldBe builder
      }
    }

    describe("#withSSLConf") {
      it("should set the SSL configurations") {
        val subject = builder.withSSLConf(
          CassandraSSLConf(
            enabled = true,
            Some("s3://bucket/key.jks"),
            Some("pwd"),
            "JKS",
            "TLS",
            Set("TLS_RSA_WITH_AES_256_CBC_SHA256")))
          .build()

        subject.s3TrustStoreUrl shouldBe Some("s3://bucket/key.jks")
        subject.trustStoreType shouldBe Some("JKS")
        subject.cipherSuites should contain("TLS_RSA_WITH_AES_256_CBC_SHA256")
      }
    }

    describe("#build") {
      it("should return a new instance of AwsS3SSLOptions") {
        builder.build() shouldBe a[AwsS3SSLOptions]
      }
    }
  }

  describe(".builder") {
    it("should return a new AwsS3SSLOptions.Builder instance") {
      AwsS3SSLOptions.builder() shouldBe a[AwsS3SSLOptions.Builder]
    }
  }
}
