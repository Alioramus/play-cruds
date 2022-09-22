package users

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

case class User(id: Int, name: String, login: String, address: String)


class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "login").write[String] and
        (JsPath \ "address").write[String]
    ) (unlift(User.unapply))

  implicit val userReads: Reads[User] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "login").read[String] and
        (JsPath \ "address").read[String]
    )(User.apply _)

  var users: List[User] = List()

  def index: Action[AnyContent] = Action { implicit request =>
    val r: Result = Ok(Json.toJson(users))
    r
  }

  def create: Action[AnyContent] = Action { implicit request =>
    users = users.appended(userReads.reads(request.body.asJson.orNull).get)
    val r: Result = Created
    r
  }

  def show(id: String): Action[AnyContent] = Action { implicit request =>
    val user = users.find(user => user.id.equals(id.toInt))
    val r: Result = Ok(Json.toJson(user))
    r
  }

  def update(id: String): Action[AnyContent] = Action { implicit request =>
    users = users.filter(user => !user.id.equals(id.toInt))
    users = users.appended(userReads.reads(request.body.asJson.orNull).get)
    val r: Result = NoContent
    r
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    users = users.filter(user => !user.id.equals(id.toInt))
    val r: Result = NoContent
    r
  }
}
