
# SBT Docker Compose Plugin

It allows you to control [docker-compose](https://docs.docker.com/compose/) via [SBT](http://www.scala-sbt.org). 

You can define your services in a simple docker-compose.yml  - the default is
$PROJECT_ROOT/docker/docker-compose.yml - and control them via SBT tasks.

## Installation

You can add it your plugins file:

```
addSbtPlugin("com.kinja" % "sbt-docker-compose" % "0.1.0")
```

## Configuration

Since it's an auto plugin with no dependencies, you don't have to import
anything. By default the plugin provides 3 tasks:
* `dockerComposeUp`: launch a new instance using your docker-compose.yml
* `dockerComposeDown`: stop and remove a running instance.
* `dockerComposeInstanceId`: return the ID the of the running docker-compose instance.

If you execute the `dockerComposeUp` then it will generate and a new instance
id using the following format: `sbt` + `SBT's PID` + `dockerComposeBaseName`.
The `dockerComposeBaseName` is "docker" by default, so for example instance id
can be `sbt75215docker`. If you are using multi project setup and every project
should a have a different instance then you can separate them by setting
`dockerComposeBaseName`.

The typical use case for this plugin is provision instances for testing. For
example, the following setup will run `dockerComposeUp` before running test,
tries to find the port number for redis service - using `docker inspect` - and
pass it to tests as a system property and shut down the running instance after
tests are completed. Unfortunately, SBT doesn't provide a way to run task after
a failed job.

```
dockerComposeOptions in Test := {
        val id = dockerComposeInstanceId.value
        Seq("-Dredis.slave.port=" + DockerComposePlugin.getPort(id, "redis"))
},
javaOptions in Test ++= (dockerComposeOptions in Test).value,
test in Test <<= {
        Def.sequential(
          (test in Test).dependsOn(dockerComposeUp),
          dockerComposeDown
        )
}
```

