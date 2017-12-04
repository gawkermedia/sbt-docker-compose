import scalariform.formatter.preferences._

sbtPlugin := true

organization := "com.kinja"
name := "sbt-docker-compose"
version := "0.1.0"

// Publishing
credentials += Credentials(Path.userHome / ".ivy2" / ".sonatype")
pgpSecretRing := file(System.getProperty("SEC_RING", ""))
pgpPublicRing := file(System.getProperty("PUB_RING", ""))
pgpPassphrase := Some(Array(System.getProperty("PGP_PASS", ""): _*))
pomExtra := {
  <url>https://github.com/gawkermedia/sbt-docker-compose</url>
  <licenses>
    <license>
      <name>BSD 3-Clause</name>
      <url>https://github.com/gawkermedia/sbt-docker-compose/blob/master/LICENSE</url>
    </license>
  </licenses>
  <scm>
    <connection>git@github.com:gawkermedia/sbt-docker-compose.git</connection>
    <developerConnection>scm:git:git@github.com:gawkermedia/sbt-docker-compose.git</developerConnection>
    <url>git@github.com:gawkermedia/sbt-docker-compose</url>
  </scm>
  <developers>
    <developer>
      <name>Kinja Developers</name>
      <organization>Gizmodo Media Group</organization>
      <organizationUrl>http://kinja.com</organizationUrl>
    </developer>
  </developers>
}

