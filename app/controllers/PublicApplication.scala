package controllers

import javax.inject.{Inject, Singleton}

import jp.t2v.lab.play2.auth.OptionalAuthElement
import models._
import models.db.Tables
import play.api.mvc.Controller
import services.db.DBService
import models.db.{AccountRole, Tables}
import play.api.Logger
import play.api.mvc.Controller
import services.db.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import FormData._
import play.api.data.Form
import utils.db.TetraoPostgresDriver.api._
import play.api.i18n.Messages.Implicits._

@Singleton
class PublicApplication @Inject()(val database: DBService, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with OptionalAuthElement {

  def index() = StackAction { implicit request =>
    Ok(views.html.index(loggedIn))
  }

  def goods(q: String = "", cat: String = "") = AsyncStack { implicit request =>
      val form: Form[GoodsSearchFormData] = searchGoodsForm.bindFromRequest.fold(
        withError => searchGoodsForm.fill(GoodsSearchFormData(q, cat)),
        ok => searchGoodsForm.fill(ok)
      )
    database.runAsync(
      Tables.Goodsview
          .filter{ row =>
              val tmp = q.split(" ").map { word => s"%$word%" }.map { word =>
                row.description.like(word) ||
                row.title.like(word) ||
                row.state.like(word) ||
                row.codeid.like(word) ||
                row.codes.like(word) ||
                row.cars.like(word) ||
                row.producedby.like(word) ||
                row.trademark.like(word)

              }
              tmp.tail.foldLeft(tmp.head)((prv, nxt)=> prv && nxt)
          }
        .filter { row => (row.category === cat) || cat.isEmpty }
        .sortBy(_.id).result
    ).map {
      rowSeq =>
          Ok( views.html.goods(loggedIn, rowSeq, form) )
        }
  }

  def showitem(itemId: Int) = AsyncStack { implicit request =>
    database.runAsync(Tables.Goodsview.filter(_.id === itemId).result.head).map { goodsRow =>
      Ok( views.html.showitem(loggedIn, goodsRow) )
    }
  }

}

