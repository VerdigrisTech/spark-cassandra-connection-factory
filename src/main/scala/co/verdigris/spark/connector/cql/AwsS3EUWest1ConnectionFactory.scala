package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3EUWest1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.EU_WEST_1.getName)
}