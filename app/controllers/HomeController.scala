package controllers

import play.api.mvc.{Action, Controller}

import scala.collection.mutable.HashMap

/**
  * A very small controller that renders a home page.
  */
class HomeController extends Controller
{
  def index = Action { implicit request =>
    val usersRef = Firebase.ref("users")

    val users: HashMap[String, User] = HashMap(("ismael", new User("a", "b", "c")),("pablo", new User("d", "e", "f")))

    usersRef.setValue(users)

    Ok(views.html.index())
  }
}
