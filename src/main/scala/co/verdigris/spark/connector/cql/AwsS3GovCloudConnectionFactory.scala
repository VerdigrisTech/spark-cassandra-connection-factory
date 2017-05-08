package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3GovCloudConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.GovCloud.getName)
}
