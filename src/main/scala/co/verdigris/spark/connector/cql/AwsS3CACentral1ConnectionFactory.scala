package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3CACentral1ConnectionFactory extends S3ConnectionFactory {
  protected override var s3Region: Option[String] = Some(Regions.CA_CENTRAL_1.getName)
}
