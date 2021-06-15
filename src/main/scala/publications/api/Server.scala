package publications.api

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import publications.api.config.Config.{HOST, PORT}
import publications.api.routes.PublicationRoutes
import publications.api.services.PublicationService

import scala.util.{Failure, Success}

class Server {

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt(HOST, PORT).bind(routes)

    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def setup: Behavior[Nothing] = {
    Behaviors.setup[Nothing] { context =>
      val publicationRegistryActor = context.spawn(PublicationService(), "PublicationRegistryActor")
      context.watch(publicationRegistryActor)

      val routes = new PublicationRoutes(publicationRegistryActor)(context.system)
      startHttpServer(routes.publicationRoutes)(context.system)

      Behaviors.empty
    }
  }
}
