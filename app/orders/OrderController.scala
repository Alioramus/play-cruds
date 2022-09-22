package orders

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

case class Order(id: Int, products: String, date: String)


class OrderController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val orderWrites: Writes[Order] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "products").write[String] and
      (JsPath \ "date").write[String]
    ) (unlift(Order.unapply))

  implicit val orderReads: Reads[Order] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "products").read[String] and
      (JsPath \ "date").read[String]
    )(Order.apply _)

  var orders: List[Order] = List()

  def index: Action[AnyContent] = Action { implicit request =>
    val r: Result = Ok(Json.toJson(orders))
    r
  }

  def create: Action[AnyContent] = Action { implicit request =>
    orders = orders.appended(orderReads.reads(request.body.asJson.orNull).get)
    val r: Result = Created
    r
  }

  def show(id: String): Action[AnyContent] = Action { implicit request =>
    val order = orders.find(order => order.id.equals(id.toInt))
    val r: Result = Ok(Json.toJson(order))
    r
  }

  def update(id: String): Action[AnyContent] = Action { implicit request =>
    orders = orders.filter(order => !order.id.equals(id.toInt))
    orders = orders.appended(orderReads.reads(request.body.asJson.orNull).get)
    val r: Result = NoContent
    r
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    orders = orders.filter(order => !order.id.equals(id.toInt))
    val r: Result = NoContent
    r
  }
}
