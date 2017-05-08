package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3CACentral1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.CA_CENTRAL_1.getName)
}
