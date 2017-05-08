package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APSouth1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.AP_SOUTH_1.getName)
}
