organization  := "com.aggregator"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "io.spray"            %%  "spray-json"    % "1.3.2",
    "org.json4s"          %%  "json4s-native" % "3.2.11",
    "com.github.nscala-time" %% "nscala-time" % "2.6.0",
    "net.sf.opencsv" % "opencsv" % "2.3"
  )
}

Revolver.settings

import com.github.retronym.SbtOneJar._

oneJarSettings