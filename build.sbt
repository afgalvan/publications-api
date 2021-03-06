lazy val akkaHttpVersion = "10.2.4"
lazy val akkaVersion     = "2.6.15"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "co.edu.unicesar",
      scalaVersion := "2.13.6"
    )),
  name := "server",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
    "ch.qos.logback"    % "logback-classic"           % "1.2.3",
    "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
    "org.scalatest"     %% "scalatest"                % "3.1.4" % Test,
    "org.scalamock"     %% "scalamock"                % "5.1.0" % Test
  )
)
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
addCommandAlias("tq", "testQuick")
addCommandAlias("tsf", "testShowFailed")

addCommandAlias("c", "compile")
addCommandAlias("tc", "test:compile")

addCommandAlias("s", "scalastyle")
addCommandAlias("ts", "test:scalastyle")

addCommandAlias("f", "scalafmt")
addCommandAlias("ft", "scalafmtTest")
