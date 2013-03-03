import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "demagog"
    val appVersion      = "1.0.0"

    val appDependencies = Seq(
    	"com.google.code.morphia" % "morphia" % "0.99",
    	"com.google.code.morphia" % "morphia-logging-slf4j" % "0.99"
    )
    
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here     
      
      resolvers += "Morphia repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
       
    )

}
