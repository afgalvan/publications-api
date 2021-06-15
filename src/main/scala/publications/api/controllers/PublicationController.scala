package publications.api.controllers

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import publications.api.services.PublicationService._
import publications.api.services.{Publication, PublicationService, Publications}

import scala.concurrent.Future

class PublicationController(publicationRegistry: ActorRef[PublicationService.Command])(implicit val system: ActorSystem[_]) {

  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getPublications: Future[Publications] = publicationRegistry.ask(GetPublications)

  def getPublication(name: String): Future[GetPublicationResponse] =
    publicationRegistry.ask(GetPublication(name, _))

  def createPublication(publication: Publication): Future[SavedPublicationResponse] =
    publicationRegistry.ask(CreatePublication(publication, _))

  def deletePublication(name: String): Future[ActionPerformed] =
    publicationRegistry.ask(DeletePublication(name, _))

}
