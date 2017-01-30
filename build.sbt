crossScalaVersions := Seq("2.11.8", "2.12.1")

lazy val scalaSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions += "-deprecation",
  scalacOptions += "-unchecked",
  scalacOptions += "-feature"
)

lazy val javaSettings = Seq(
  crossPaths := false,
  autoScalaLibrary := false
)

lazy val Versions = new {
  val dropwizard = "3.1.2"
}

lazy val commonSettings = Seq(
  organization := "com.avast.metrics",
  version := sys.env.getOrElse("TRAVIS_TAG", "0.1-SNAPSHOT"),
  description := "Library for conversion between GPB and Scala case classes",

  licenses ++= Seq("MIT" -> url(s"https://github.com/avast/metrics/blob/${version.value}/LICENSE")),
  publishArtifact in Test := false,
  bintrayOrganization := Some("avast"),
  pomExtra := (
    <scm>
      <url>git@github.com:avast/metrics.git</url>
      <connection>scm:git:git@github.com:avast/metrics.git</connection>
    </scm>
      <developers>
        <developer>
          <id>avast</id>
          <name>Jakub Janecek, Avast Software s.r.o.</name>
          <url>https://www.avast.com</url>
        </developer>
      </developers>
    )
)

lazy val root = (project in file("."))
  .aggregate(api, scalaApi, core, dropwizard, dropwizardGraphite)

lazy val api = (project in file("api")).
  settings(
    commonSettings,
    javaSettings,
    name := "metrics-api"
  )

lazy val scalaApi = (project in file("scala-api")).
  settings(
    commonSettings,
    scalaSettings,

    name := "metrics-scala",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "org.mockito" % "mockito-all" % "1.10.19" % "test"
    )
  ).dependsOn(api)

lazy val core = (project in file("core")).
  settings(
    commonSettings,
    javaSettings,
    name := "metrics-core"
  ).dependsOn(api)

lazy val dropwizard = (project in file("dropwizard")).
  settings(
    commonSettings,
    javaSettings,
    name := "metrics-dropwizard",
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-core" % Versions.dropwizard,
      "org.slf4j" % "slf4j-api" % "1.7.22",

      "junit" % "junit" % "4.12" % "test",
      "ch.qos.logback" % "logback-classic" % "1.1.8" % "test"
    )
  ).dependsOn(core)

lazy val dropwizardGraphite = (project in file("dropwizard-graphite")).
  settings(
    commonSettings,
    javaSettings,
    name := "metrics-dropwizard-graphite",
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-graphite" % Versions.dropwizard
    )
  ).dependsOn(dropwizard)
