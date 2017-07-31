name := """scoins"""

organization := "com.ruimo"

crossScalaVersions := List("2.11.8", "2.12.3") 

publishTo := Some(
  Resolver.file(
    "recoengcommon",
    new File(Option(System.getenv("RELEASE_DIR")).getOrElse("/tmp"))
  )
)

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % "test"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.3"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.9.4" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

scalacOptions ++= Seq("-feature")
