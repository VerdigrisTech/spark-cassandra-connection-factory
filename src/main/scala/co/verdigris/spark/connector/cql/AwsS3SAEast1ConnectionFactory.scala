package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

object AwsS3SAEast1ConnectionFactory extends S3ConnectionFactory {
  this.s3Region = Some(Regions.SA_EAST_1.getName)
}
