package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APSouth1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.AP_SOUTH_1.getName)
}
