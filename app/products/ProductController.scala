package products

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

case class Product(id: Int, name: String, category: String, price: Int)


class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val productWrites: Writes[Product] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "category").write[String] and
      (JsPath \ "price").write[Int]
    ) (unlift(Product.unapply))

  implicit val productReads: Reads[Product] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "category").read[String] and
        (JsPath \ "price").read[Int]
    )(Product.apply _)

  var products: List[Product] = List()

  def index: Action[AnyContent] = Action { implicit request =>
    val r: Result = Ok(Json.toJson(products))
    r
  }

  def create: Action[AnyContent] = Action { implicit request =>
    products = products.appended(productReads.reads(request.body.asJson.orNull).get)
    val r: Result = Created
    r
  }

  def show(id: String): Action[AnyContent] = Action { implicit request =>
    val product = products.find(product => product.id.equals(id.toInt))
    val r: Result = Ok(Json.toJson(product))
    r
  }

  def update(id: String): Action[AnyContent] = Action { implicit request =>
    products = products.filter(product => !product.id.equals(id.toInt))
    products = products.appended(productReads.reads(request.body.asJson.orNull).get)
    val r: Result = NoContent
    r
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    products = products.filter(product => !product.id.equals(id.toInt))
    val r: Result = NoContent
    r
  }
}
