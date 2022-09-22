package products

import io.lemonlabs.uri.typesafe.dsl.pathPartToUrlDsl
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class ProductRouter @Inject()(controller: ProductController) extends SimpleRouter {
  val prefix = "products"

  def link(id: String): String = {
    val url = prefix / id
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") =>
      controller.index

    case POST(p"/") =>
      controller.create

    case GET(p"/$id") =>
      controller.show(id)

    case PUT(p"/$id") =>
      controller.update(id)

    case DELETE(p"/$id") =>
      controller.delete(id)
  }

}