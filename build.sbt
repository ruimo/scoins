name := """scoins"""

organization := "com.ruimo"

crossScalaVersions := List("2.11.8", "2.12.3", "2.13.0") 

scalaVersion := "2.12.3"

publishTo := Some(
  Resolver.file(
    "scoins",
    new File(Option(System.getenv("RELEASE_DIR")).getOrElse("/tmp"))
  )
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.7.0" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

scalacOptions ++= Seq("-feature")
