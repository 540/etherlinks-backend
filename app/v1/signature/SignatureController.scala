package v1.signature

import javax.inject.Inject

import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class SignatureFormInput(uri: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class SignatureController @Inject()(
                                     action: SignatureAction,
                                     handler: SignatureResourceHandler)(implicit ec: ExecutionContext)
    extends Controller {

  private val form: Form[SignatureFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "uri" -> nonEmptyText
      )(SignatureFormInput.apply)(SignatureFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = {
    action.async { implicit request =>
      handler.find.map { signatures =>
        Ok(Json.toJson(signatures))
      }
    }
  }

  def process: Action[AnyContent] = {
    action.async { implicit request =>
      processJsonPost()
    }
  }

  def show(id: String): Action[AnyContent] = {
    action.async { implicit request =>
      handler.lookup(id).map { signature =>
        Ok(Json.toJson(signature))
      }
    }
  }

  private def processJsonPost[A]()(
      implicit request: SignatureRequest[A]): Future[Result] = {
    def failure(badForm: Form[SignatureFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: SignatureFormInput) = {
      handler.create(input).map { signature =>
        Created(Json.toJson(signature)).withHeaders(LOCATION -> signature.uri)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
