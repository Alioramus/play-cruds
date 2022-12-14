name := "play_play"
 
version := "1.0" 
      
lazy val `play_play` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice , "io.lemonlabs" %% "scala-uri" % "4.0.2")
      