package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3USEast2ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.US_EAST_2.getName)
}
