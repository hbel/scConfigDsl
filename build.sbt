import Dependencies._

lazy val commonSettings = Seq(
  organization := "org.hbel",
  scalaVersion := "2.12.3",
  version      := "0.1.0",
  scalacOptions += "-feature",
  javaOptions += "-Dconfig.file=test/resources/application.conf"
)

lazy val root = (project in file(".")).
  settings(
    commonSettings,
    name := "scConfigDsl",
    libraryDependencies ++= Seq(scalaTest % Test,
      scalaCheck % Test,
      logback,
      scalaLogging,
      typesafeConfig)
  )
