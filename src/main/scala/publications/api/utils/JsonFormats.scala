package publications.api.utils

import publications.api.services.PublicationService.ActionPerformed
import publications.api.services.{Publication, Publications}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonFormats  {
  import DefaultJsonProtocol._

  implicit val publicationJsonFormat: RootJsonFormat[Publication] = jsonFormat5(Publication)
  implicit val publicationsJsonFormat: RootJsonFormat[Publications] = jsonFormat1(Publications)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}
