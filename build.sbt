import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

name := "hello-play-service"

version := "0.3"

scalaVersion := "2.13.1"

val Lombok = "1.16.16"

lazy val `hello-play-service` = (project in file("."))
  .enablePlugins(PlayMinimalJava)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      logback,
      javaJpa,
      jdbc,
      javaWs,
      ehcache,
      "org.projectlombok" % "lombok" % Lombok,
      "io.dropwizard.metrics" % "metrics-core" % "3.2.6",
      "net.jodah" % "failsafe" % "1.0.5",
    )
  )

libraryDependencies += "com.h2database" % "h2" % "1.4.199"
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.4.0.Final"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.8"

daemonUser in Docker := "root"
dockerBaseImage := "openjdk:8-jre-alpine"
dockerRepository := Some("yoeluk")
dockerExposedPorts := Seq(9000)
dockerCmd := Seq("-Dpidfile.path=/dev/null")
dockerEntrypoint := Seq("bin/%s" format executableScriptName.value)
dockerCommands := dockerCommands.value.flatMap {
  case cmd@Cmd("FROM", _) => List(cmd, ExecCmd("RUN", "apk", "--update", "add", "bash"))
  case other => List(other)
}

ThisBuild / javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"