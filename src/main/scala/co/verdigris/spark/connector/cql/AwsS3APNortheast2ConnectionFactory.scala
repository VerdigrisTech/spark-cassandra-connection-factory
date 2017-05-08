package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APNortheast2ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.AP_NORTHEAST_2.getName)
}
