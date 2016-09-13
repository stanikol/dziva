import com.typesafe.config.ConfigFactory

name := """play-scala-slickpg-play2auth"""

//common settings for the project and subprojects
lazy val commonSettings = Seq(
	organization := "eu.tetrao",
	version := "0.1.1",
	scalaVersion := "2.11.8",
	scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-target:jvm-1.8")
)

lazy val root = (project in file("."))
	.settings(commonSettings: _*)
	.settings(routesGenerator := InjectedRoutesGenerator)
	.settings(
		libraryDependencies += "com.typesafe.slick" %% "slick" % "3.1.1",
		libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
		libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.14.1",
		libraryDependencies += "com.github.tminglei" %% "slick-pg_date2" % "0.14.1",
		libraryDependencies += "com.typesafe.play" %% "play-slick" % "2.0.2",
		libraryDependencies += "jp.t2v" %% "play2-auth" % "0.14.2",
		libraryDependencies += play.sbt.Play.autoImport.cache,
		libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "2.6",
    libraryDependencies += "org.webjars" %% "webjars-play" % "2.5.0",
		 libraryDependencies += "org.webjars" % "foundation" % "6.2.3", /// snc
		libraryDependencies +=  "org.webjars" % "foundation-icon-fonts" % "d596a3cfb3" /// snc
	)
  .enablePlugins(PlayScala)

//to generate models/db/Tables.scala
addCommandAlias("tables", "run-main utils.db.SourceCodeGenerator")


fork in run := true

