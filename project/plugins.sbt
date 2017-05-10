resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"

addSbtPlugin("ohnosequences" % "sbt-s3-resolver" % "0.15.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
