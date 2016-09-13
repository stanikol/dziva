package controllers

import javax.inject.{Inject, Singleton}

import jp.t2v.lab.play2.auth.OptionalAuthElement
import models.GoodsItem
import models.{Entity, FormData, FormDataAccount, Message}
import models.db.Tables
import play.api.mvc.Controller
import services.db.DBService
import models.db.{AccountRole, Tables}
import play.api.Logger
import play.api.mvc.Controller
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class PublicApplication @Inject()(val database: DBService, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with OptionalAuthElement {

  def index() = StackAction { implicit request =>
    Ok(views.html.index(loggedIn))
  }

  def goods() = AsyncStack { implicit request =>
        database.runAsync(Tables.Goods.sortBy(_.id).result).map { rowSeq =>
          val goodsSeq: Seq[Entity[GoodsItem]] = rowSeq.map(GoodsItem(_))
          Ok( views.html.goods(loggedIn, goodsSeq) )
        }
      }

  def showitem(itemId: Int) = AsyncStack { implicit request =>
    database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map { row =>
      Ok( views.html.showitem(loggedIn, GoodsItem(row)) )
    }
  }

}

