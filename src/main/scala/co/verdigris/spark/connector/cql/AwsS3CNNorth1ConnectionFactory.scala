package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3CNNorth1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.CN_NORTH_1.getName)
}
