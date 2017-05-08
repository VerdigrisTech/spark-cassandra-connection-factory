package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3USWest1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.US_WEST_1.getName)
}
