package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3EUWest2ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.EU_WEST_2.getName)
}
