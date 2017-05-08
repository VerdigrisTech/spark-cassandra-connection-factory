package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3CNNorth1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.CN_NORTH_1.getName)
}
