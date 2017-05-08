package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3GovCloudConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.GovCloud.getName)
}
