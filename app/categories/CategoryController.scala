package categories

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

case class Category(id: Int, name: String, description: String)


class CategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val categoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").write[String]
    ) (unlift(Category.unapply))

  implicit val categoryReads: Reads[Category] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String]
    )(Category.apply _)

  var categories: List[Category] = List()

  def index: Action[AnyContent] = Action { implicit request =>
    val r: Result = Ok(Json.toJson(categories))
    r
  }

  def create: Action[AnyContent] = Action { implicit request =>
    categories = categories.appended(categoryReads.reads(request.body.asJson.orNull).get)
    val r: Result = Created
    r
  }

  def show(id: String): Action[AnyContent] = Action { implicit request =>
    val category = categories.find(category => category.id.equals(id.toInt))
    val r: Result = Ok(Json.toJson(category))
    r
  }

  def update(id: String): Action[AnyContent] = Action { implicit request =>
    categories = categories.filter(category => !category.id.equals(id.toInt))
    categories = categories.appended(categoryReads.reads(request.body.asJson.orNull).get)
    val r: Result = NoContent
    r
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    categories = categories.filter(category => !category.id.equals(id.toInt))
    val r: Result = NoContent
    r
  }
}
