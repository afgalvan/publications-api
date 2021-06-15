package publications.api

import akka.actor.typed.ActorSystem

object App {

  def main(args: Array[String]): Unit = {

    val rootBehavior = new Server().setup
    ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }
}
