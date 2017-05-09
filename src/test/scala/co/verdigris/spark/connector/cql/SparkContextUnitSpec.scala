package co.verdigris.spark.connector.cql

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FunSpec}

trait SparkContextUnitSpec extends FunSpec with BeforeAndAfterAll {
  var master: Option[String] = None
  var appName: Option[String] = None
  var sparkConf: SparkConf = _
  var sc: SparkContext = _

  override def beforeAll {
    sparkConf = new SparkConf()
      .setMaster(master.getOrElse("local[*]"))
      .setAppName(appName.getOrElse("Spark Context Tests"))
    sc = SparkContext.getOrCreate(sparkConf)
  }

  override def afterAll: Unit = {
    sc.cancelAllJobs()
  }
}
