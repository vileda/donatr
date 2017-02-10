import Dependencies._
import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport.{fullOptJS, packageMinifiedJSDependencies}


lazy val donatr = (project in file("."))
  .aggregate(donatrCore, vertxServer, donatrUi)
  .settings(
    inThisBuild(List(
      organization := "de.fnordeingang",
      //scalaOrganization := "org.typelevel",
      scalaVersion := "2.12.1",
      version := "0.1.0-SNAPSHOT",
      scalacOptions ++= Seq("-deprecation", "-feature"),
      resolvers += "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/",
      test in assembly := {}
    )),
    herokuAppName in Compile := "donatr",
    herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value),
    mainClass in assembly := Some("donatr.DonatrVertxServer"),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case PathList("META-INF", xs @ _*) => MergeStrategy.last
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
      case "codegen.json" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  ).dependsOn(donatrCore, vertxServer, donatrUi)

lazy val donatrCore = (project in file("./donatr-core")).
  settings(
    resolvers += "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.7.0",
      "io.circe" %% "circe-generic" % "0.7.0",
      "io.circe" %% "circe-parser" % "0.7.0",
      "com.typesafe.slick" %% "slick" % "3.2.0-M2",
      "org.slf4j" % "slf4j-api" % "1.7.22",
      "ch.qos.logback" % "logback-classic" % "1.1.10",
      "com.h2database" % "h2" % "1.4.193",
      scalaTest % Test,
      "org.scalacheck" %% "scalacheck" % "1.13.4" % Test
    )
  )

lazy val vertxServer = (project in file("./vertx-server")).
  settings(
    resolvers += "Sonatype SNAPSHOTS" at "https://oss.sonatype.org/content/repositories/snapshots/",
    mainClass in assembly := Some("donatr.DonatrVertxServer"),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case PathList("META-INF", xs @ _*) => MergeStrategy.last
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
      case "codegen.json" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    unmanagedResourceDirectories in Compile += { baseDirectory.value / "../donatr-ui/target/scala-2.12/public" },
    libraryDependencies ++= Seq(
      Dependencies.vertxLangScala.exclude("io.vertx", "vertx-codegen"),
      Dependencies.vertxCodegen,
      Dependencies.vertxWeb.exclude("io.vertx", "vertx-codegen"),
      "io.circe" %% "circe-core" % "0.7.0",
      "io.circe" %% "circe-generic" % "0.7.0",
      "io.circe" %% "circe-parser" % "0.7.0",
      scalaTest % Test,
      "org.scalacheck" %% "scalacheck" % "1.13.4" % Test
    )
  ).dependsOn(donatrCore)

lazy val donatrUi = (project in file("./donatr-ui")).
  settings(
    libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-html" % "latest.integration",
      "com.github.japgolly.scalacss" %%% "core" % "0.5.1"
    ),
    jsDependencies += "org.webjars.npm" % "spark-md5" % "2.0.2" / "spark-md5.js",
    emitSourceMaps := true,
    artifactPath in (Compile, fastOptJS) :=
      ((crossTarget in (Compile, fastOptJS)).value / "public" / "webroot2" /
        ((moduleName in fastOptJS).value + "-opt.js")),
    artifactPath in (Compile, fullOptJS) :=
      ((crossTarget in (Compile, fullOptJS)).value / "public" / "webroot2" /
        ((moduleName in fullOptJS).value + "-opt.js")),
    artifactPath in (Compile, packageMinifiedJSDependencies) :=
      ((crossTarget in (Compile, packageMinifiedJSDependencies)).value / "public" / "webroot2" /
        ((moduleName in packageMinifiedJSDependencies).value + "-jsdeps.js"))
  ).enablePlugins(ScalaJSPlugin)
