package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APSoutheast1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.AP_SOUTHEAST_1.getName)
}
