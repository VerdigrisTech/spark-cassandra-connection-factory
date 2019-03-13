package co.verdigris.spark.connector.cql

import com.amazonaws.regions.Regions

@deprecated("Use co.verdigris.spark.connector.cql.S3ConnectionFactory with spark.executorEnv.AWS_REGION instead", "0.4.0")
object AwsS3USEast2ConnectionFactory extends S3ConnectionFactory {
}
