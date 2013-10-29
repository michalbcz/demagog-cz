import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "demagog"
    val appVersion      = "1.0.0"

    val appDependencies = Seq(
    	javaCore,
    	"com.google.code.morphia" % "morphia" % "0.99",
    	"com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
      	"net.tanesha.recaptcha4j" % "recaptcha4j" % "0.0.7",
      	"commons-collections" % "commons-collections" % "3.2.1"
    )
    
    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here     
      
      resolvers += "Morphia repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
    )

}
