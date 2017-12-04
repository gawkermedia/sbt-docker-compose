package com.kinja.sbtdockercompose

import sbt._
import Keys._

object DockerComposePlugin extends AutoPlugin {
  object autoImport {
    val dockerComposeUp = taskKey[Unit](
      "Start an instance in Docker")
    val dockerComposeDown = taskKey[Unit](
      "Stop and remove a Docker instance")
    val dockerComposePath = settingKey[File](
      "Full path to the Compose File to use to create your test instance")
    val dockerComposeBaseName = taskKey[String](
      "Base name to generate docker instance name")
    val dockerComposeInstanceId = taskKey[String](
      "Id of the running instance")
    val dockerComposeOptions = taskKey[Seq[String]](
      "Docker instance settings to java options")
  }
  import autoImport._
  override lazy val buildSettings = Seq(
    dockerComposeBaseName := "docker",
    dockerComposePath := file(".") / "docker/docker-compose.yml",
    dockerComposeInstanceId := {
      val baseName = dockerComposeBaseName.value
      val pid: Long = Seq("sh", "-c", "echo $PPID").!!.trim.toLong
      s"sbt$pid${baseName.replaceAll("-", "")}"
    },
    dockerComposeUp := {
      val instanceName = dockerComposeInstanceId.value
      val composePath = dockerComposePath.value.getAbsolutePath
      Process(s"docker-compose -p $instanceName -f $composePath up -d").!
    },
    dockerComposeDown := {
      val instanceName = dockerComposeInstanceId.value
      val composePath = dockerComposePath.value.getAbsolutePath
      Process(s"docker-compose -p $instanceName -f $composePath down").!
    })
  override def trigger = allRequirements

  lazy val portTemplate =
    "'{{range .NetworkSettings.Ports}}{{range .}}{{.HostPort}}{{end}}{{end}}'"

  def inspect(containerId: String, template: String): String = {
    val args = Seq("inspect", "-f", template, containerId)
    Process("docker", args).!!.trim
  }
  def getInstancePort(containerId: String): String = {
    inspect(containerId, portTemplate).replaceAll("'", "")
  }
  def getContainerId(instanceName: String, service: String): String = {
    instanceName + "_" + service + "_1"
  }
  def getPort(instanceName: String, service: String): String = {
    getInstancePort(getContainerId(instanceName, service))
  }
}
