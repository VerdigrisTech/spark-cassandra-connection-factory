package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APSoutheast1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.AP_SOUTHEAST_1.getName)
}
