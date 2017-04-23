package v1.signature

import java.util.Calendar
import javax.inject.{Inject, Provider}

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

import scala.util.Random

/**
  * DTO for displaying post information.
  */
case class SignatureResource(id: String, uri: String, date: Long)

object SignatureResource {

  /**
    * Mapping to write a PostResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[SignatureResource] {
    def writes(signature: SignatureResource): JsValue = {
      Json.obj(
        "id" -> signature.id,
        "uri" -> signature.uri,
        "date" -> signature.date
      )
    }
  }
}

/**
  * Controls access to the backend data, returning [[SignatureResource]]
  */
class SignatureResourceHandler @Inject()(
                                          routerProvider: Provider[SignatureRouter],
                                          signatureRepository: SignatureRepository)(implicit ec: ExecutionContext) {

  def create(signatureInput: SignatureFormInput): Future[SignatureResource] = {
    val data = SignatureData(SignatureId(Random.nextInt(100000).toString), signatureInput.uri, Calendar.getInstance().getTimeInMillis)
    // We don't actually create the post, so return what we have
    signatureRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(id: String): Future[Option[SignatureResource]] = {
    val postFuture = signatureRepository.get(SignatureId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def find: Future[Iterable[SignatureResource]] = {
    signatureRepository.list().map { postDataList =>
      postDataList.map(postData => createPostResource(postData))
    }
  }

  private def createPostResource(p: SignatureData): SignatureResource = {
    SignatureResource(p.id.toString, p.uri, p.date)
  }

}
