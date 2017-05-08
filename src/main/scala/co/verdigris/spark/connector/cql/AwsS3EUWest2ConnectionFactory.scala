package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3EUWest2ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.EU_WEST_2.getName)
}
