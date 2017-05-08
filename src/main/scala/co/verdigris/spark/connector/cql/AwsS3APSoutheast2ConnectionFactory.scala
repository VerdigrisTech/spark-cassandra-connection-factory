package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3APSoutheast2ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.AP_SOUTHEAST_2.getName)
}
