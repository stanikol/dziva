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

import scala.concurrent.ExecutionContext.Implicits.global
import FormData._
import play.api.data.Form
import utils.db.TetraoPostgresDriver.api._


@Singleton
class PublicApplication @Inject()(val database: DBService, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with OptionalAuthElement {

  def index() = StackAction { implicit request =>
    Ok(views.html.index(loggedIn))
  }

  def goods(q: String = "", cat: String = "") = AsyncStack { implicit request =>
      val form: Form[GoodsSearchFormData] = searchGoodsForm.bindFromRequest.fold(
        withError => searchGoodsForm.fill(GoodsSearchFormData()),
        ok => searchGoodsForm.fill(ok)
      )
    val words = q.split(" ")
    implicit val goodsCategoriesTypeMapper = MappedColumnType.base[models.db.GoodsCategories.Value, String](
      { g => g.toString },    //
      { s => models.db.GoodsCategories.withName(s) }
    )

    database.runAsync(Tables.Goods.filter { row: Tables.Goods =>
          val tmp = words map { word =>
            (row.title ++ row.state ++ row.codeid ++ row.cars
                ++ row.producedby ++ row.codes ++ row.trademark
            ).toLowerCase.like(s"%${word.toLowerCase}%")
          }
          tmp.foldLeft(tmp.head.getOrElse(false))((prv, nxt)=> prv && nxt.getOrElse(false))
        }.filter { row => if(cat.isEmpty) true:Rep[Boolean] else (row.category === models.db.GoodsCategories.withName(cat))
        }.sortBy(_.id).result).map { rowSeq =>
          val goodsSeq: Seq[Entity[GoodsItem]] = rowSeq.map(GoodsItem(_))
          Ok( views.html.goods(loggedIn, goodsSeq, form) )
        }
      }

  def showitem(itemId: Int) = AsyncStack { implicit request =>
    database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map { row =>
      Ok( views.html.showitem(loggedIn, GoodsItem(row)) )
    }
  }

}

