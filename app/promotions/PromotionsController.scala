package promotions

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject

case class Promotion(id: Int, name: String, description: String)


class PromotionController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  implicit val promotionWrites: Writes[Promotion] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").write[String]
    ) (unlift(Promotion.unapply))

  implicit val promotionReads: Reads[Promotion] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String]
    )(Promotion.apply _)

  var promotions: List[Promotion] = List()

  def index: Action[AnyContent] = Action { implicit request =>
    val r: Result = Ok(Json.toJson(promotions))
    r
  }

  def create: Action[AnyContent] = Action { implicit request =>
    promotions = promotions.appended(promotionReads.reads(request.body.asJson.orNull).get)
    val r: Result = Created
    r
  }

  def show(id: String): Action[AnyContent] = Action { implicit request =>
    val promotion = promotions.find(promotion => promotion.id.equals(id.toInt))
    val r: Result = Ok(Json.toJson(promotion))
    r
  }

  def update(id: String): Action[AnyContent] = Action { implicit request =>
    promotions = promotions.filter(promotion => !promotion.id.equals(id.toInt))
    promotions = promotions.appended(promotionReads.reads(request.body.asJson.orNull).get)
    val r: Result = NoContent
    r
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    promotions = promotions.filter(promotion => !promotion.id.equals(id.toInt))
    val r: Result = NoContent
    r
  }
}
