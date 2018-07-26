
import Settings._

lazy val `spark-stubs` = project
  .underModules
  .settings(
    shared,
    libraryDependencies += Deps.sparkSql % "provided"
  )

lazy val core = project
  .in(file("modules/core"))
  .dependsOn(`spark-stubs`)
  .settings(
    shared,
    name := "ammonite-spark",
    generatePropertyFile("org/apache/spark/sql/ammonitesparkinternals/ammonite-spark.properties"),
    libraryDependencies ++= Seq(
      Deps.ammoniteRepl % "provided",
      Deps.sparkSql % "provided",
      Deps.jettyServer
    )
  )

lazy val tests = project
  .underModules
  .settings(
    shared,
    dontPublish,
    generatePropertyFile("ammonite/ammonite-spark.properties"),
    generateDependenciesFile,
    testSettings,
    libraryDependencies ++= Seq(
      Deps.ammoniteRepl,
      Deps.utest
    )
  )

lazy val `standalone-tests` = project
  .dependsOn(tests)
  .underModules
  .settings(
    shared,
    dontPublish,
    testSettings
  )

lazy val `yarn-tests` = project
  .dependsOn(tests)
  .underModules
  .settings(
    shared,
    dontPublish,
    testSettings
  )

lazy val `ammonite-spark` = project
  .in(file("."))
  .aggregate(
    core,
    `spark-stubs`,
    tests
  )
  .settings(
    shared,
    dontPublish
  )
