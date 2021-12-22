inThisBuild(
  Seq(
    name := """scoins""",
    homepage := Some(url("https://github.com/ruimo/scoins")),
    organization := "com.ruimo",
    description := "Tiny functions for Scala.",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository := "https://s01.oss.sonatype.org/service/local",
    crossScalaVersions := List("2.11.12", "2.12.15", "2.13.7", "3.0.2"),
    libraryDependencies ++= Seq(
      "jakarta.xml.bind" % "jakarta.xml.bind-api" % "3.0.0",
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "org.scalactic" %% "scalactic" % "3.2.10" % Test,
      "org.mockito" % "mockito-all" % "1.9.5" % Test
    ),
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature"),
    Test / scalacOptions ++= Seq("-Yrangepos"),
    licenses := Seq(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "ruimo",
        "Shisei Hanai",
        "ruimo.uno@gmail.com",
        url("https://github.com/ruimo")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ruimo/scoins"),
        "scm:git@github.com:ruimo/scoins.git"
      )
    ),
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository := "https://s01.oss.sonatype.org/service/local",
    releaseProcess -= publishArtifacts
  )
)

