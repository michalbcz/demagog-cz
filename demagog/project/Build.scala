import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "demagog"
    val appVersion      = "1.0.0"

    val appDependencies = Seq(
    	"com.google.code.morphia" % "morphia" % "0.99",
    	"com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
		"net.tanesha.recaptcha4j" % "recaptcha4j" % "0.0.7",
		"org.pac4j" % "play-pac4j_java" % "1.1.0-SNAPSHOT",
		"org.pac4j" % "pac4j-oauth" % "1.4.0-SNAPSHOT"
    )
    
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here     
      
      resolvers += "Morphia repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
	  resolvers += "Sonatype snapshots repository" at "http://oss.sonatype.org/content/repositories/snapshots/"
    )

}
