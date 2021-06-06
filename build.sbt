name := """scoins"""

organization := "com.ruimo"

crossScalaVersions := List("2.11.12", "2.12.14", "2.13.6", "3.0.0") 

scalaVersion := "3.0.0"

publishTo := Some(
  Resolver.file(
    "scoins",
    new File(Option(System.getenv("RELEASE_DIR")).getOrElse("/tmp"))
  )
)

libraryDependencies ++= Seq(
  "org.specs2" % "specs2-core_2.13" % "4.12.0" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

scalacOptions ++= Seq("-feature")
