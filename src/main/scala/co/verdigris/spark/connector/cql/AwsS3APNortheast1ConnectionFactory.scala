package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APNortheast1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.AP_NORTHEAST_1.getName)
}