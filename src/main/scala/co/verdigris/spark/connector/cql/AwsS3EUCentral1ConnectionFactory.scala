package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3EUCentral1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.EU_CENTRAL_1.getName)
}
