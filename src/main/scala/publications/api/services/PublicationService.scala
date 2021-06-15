package publications.api.services

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import scala.collection.immutable

final case class Publication(isbn: String, title: String, author: String, year: Int, price: Double)
final case class Publications(publication: immutable.Seq[Publication])

object PublicationService {

  sealed trait Command

  final case class GetPublications(replyTo: ActorRef[Publications]) extends Command

  final case class CreatePublication(publication: Publication, replyTo: ActorRef[SavedPublicationResponse]) extends Command

  final case class GetPublication(ISBN: String, replyTo: ActorRef[GetPublicationResponse]) extends Command

  final case class DeletePublication(ISBN: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class SavedPublicationResponse(maybePublication: Publication)

  final case class GetPublicationResponse(maybePublication: Option[Publication])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(publications: Set[Publication]): Behavior[Command] = Behaviors.receiveMessage {
    case GetPublications(replyTo) =>
      replyTo ! Publications(publications.toSeq)
      Behaviors.same
    case CreatePublication(publication, replyTo) =>
      replyTo ! SavedPublicationResponse(publication)
      registry(publications + publication)
    case GetPublication(isbn, replyTo) =>
      replyTo ! GetPublicationResponse(publications.find(_.isbn == isbn))
      Behaviors.same
    case DeletePublication(isbn, replyTo) =>
      replyTo ! ActionPerformed(s"Publication $isbn deleted.")
      registry(publications.filterNot(_.isbn == isbn))
  }
}
