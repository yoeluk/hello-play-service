name := "hello-play-service"

version := "0.1"

scalaVersion := "2.13.1"

val Lombok = "1.16.16"

lazy val `hello-play-service` = (project in file("."))
  .enablePlugins(PlayMinimalJava)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      logback,
      "org.projectlombok" % "lombok" % Lombok,
    )
  )