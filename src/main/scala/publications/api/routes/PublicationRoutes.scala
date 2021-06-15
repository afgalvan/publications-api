package publications.api.routes

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import publications.api.controllers.PublicationController
import publications.api.services.{Publication, PublicationService}
import publications.api.utils.JsonFormats

class PublicationRoutes(publicationRegistry: ActorRef[PublicationService.Command])(implicit val system: ActorSystem[_]) {

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  val controller = new PublicationController(publicationRegistry)

  // GET
  def getPublications: Route = get {
    complete(controller.getPublications)
  }

  // POST
  def postPublication: Route = post {
    entity(as[Publication]) { publication =>
      onSuccess(controller.createPublication(publication)) { _ =>
        complete(StatusCodes.Created, publication)
      }
    }
  }

  // GET
  def getPublicationByISBN(ISBN: String): Route = get {
    rejectEmptyResponse {
      onSuccess(controller.getPublication(ISBN)) { response =>
        complete(response.maybePublication)
      }
    }
  }

  // DELETE
  def deletePublicationByISBN(ISBN: String): Route = delete {
    onSuccess(controller.deletePublication(ISBN)) { performed =>
      complete((StatusCodes.OK, performed))
    }
  }

  val publicationRoutes: Route = pathPrefix("publications") {
    concat(
      pathEnd {
        concat(
          getPublications,
          postPublication
        )
      },

      path(Segment) { ISBN =>
        concat(
          getPublicationByISBN(ISBN),
          deletePublicationByISBN(ISBN)
        )
      })
  }
}
