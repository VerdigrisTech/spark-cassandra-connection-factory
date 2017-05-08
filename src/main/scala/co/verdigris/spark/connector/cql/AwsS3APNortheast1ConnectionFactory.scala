package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APNortheast1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.AP_NORTHEAST_1.getName)
}
