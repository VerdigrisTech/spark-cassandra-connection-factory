package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3USWest1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.US_WEST_1.getName)
}
