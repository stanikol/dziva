// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" %% "sbt-plugin" % "2.5.4")

// To check dependency updates: https://github.com/rtimush/sbt-updates
addSbtPlugin("com.timushev.sbt" %% "sbt-updates" % "0.1.10")

addSbtPlugin("com.heroku" % "sbt-heroku" % "1.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.6")