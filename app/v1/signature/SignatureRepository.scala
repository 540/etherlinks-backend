package v1.signature

import java.util.{Calendar, UUID}
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

final case class SignatureData(id: SignatureId, uri: String, date: Long)

class SignatureId private(val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object SignatureId {
  def apply(raw: String): SignatureId = {
    require(raw != null)
    new SignatureId(Integer.parseInt(raw))
  }
}

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait SignatureRepository {
  def create(data: SignatureData): Future[SignatureId]

  def list(): Future[Iterable[SignatureData]]

  def get(id: SignatureId): Future[Option[SignatureData]]
}

/**
  * A trivial implementation for the Post Repository.
  */
@Singleton
class SignatureRepositoryImpl @Inject() extends SignatureRepository {

  private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  private val signatureList = List(
    SignatureData(SignatureId("1"), "https://twitter.com/the_melee/status/856059487052017664", 246237453264l),
    SignatureData(SignatureId("2"), "https://twitter.com/the_melee/status/856059487052017664", 246237453264l),
    SignatureData(SignatureId("3"), "https://twitter.com/the_melee/status/856059487052017664", 246237453264l),
    SignatureData(SignatureId("4"), "https://twitter.com/the_melee/status/856059487052017664", 246237453264l),
    SignatureData(SignatureId("5"), "https://twitter.com/the_melee/status/856059487052017664", 246237453264l)
  )

  override def list(): Future[Iterable[SignatureData]] = {
    Future.successful {
      logger.trace(s"list: ")
      signatureList
    }
  }

  override def get(id: SignatureId): Future[Option[SignatureData]] = {
    Future.successful {
      logger.trace(s"get: id = $id")
      signatureList.find(signature => signature.id == id)
    }
  }

  def create(data: SignatureData): Future[SignatureId] = {
    Future.successful {
      val signature = Signature(data.uri, UUID.randomUUID().toString, data.date)

      val signaturesRef = Firebase.ref("signatures/" +data.id)

      signaturesRef.setValue(signature.toBean)

      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
