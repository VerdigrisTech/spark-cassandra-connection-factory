package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3USWest2ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.US_WEST_2.getName)
}
