package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3USEast2ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.US_EAST_2.getName)
}
