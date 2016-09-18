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
    val words = q.split(" ")
//    implicit val goodsCategoriesTypeMapper = MappedColumnType.base[models.db.GoodsCategories.Value, String](
//      { g => g.toString },    //
//      { s => models.db.GoodsCategories.withName(s) }
//    )

    database.runAsync(Tables.Goods.filter { row: Tables.Goods =>
          val tmp = words map { word =>
            (row.description ++ row.title ++ row.state.getOrElse("") ++ row.codeid.getOrElse("") ++ row.cars.getOrElse("")
                ++ row.producedby.getOrElse("") ++ row.codes.getOrElse("") ++ row.trademark.getOrElse("")
            ).toLowerCase.like(s"%${word.toLowerCase}%")
          }
          tmp.foldLeft(tmp.head)((prv, nxt)=> prv && nxt)
        }.filter { row => if(cat.nonEmpty) (row.category === cat)
                           else true: Rep[Boolean]
        }.sortBy(_.id).joinLeft(Tables.Pics).on((g, p)=> g.pic === p.id).result
    ).map { rowSeq =>
          val goodsSeq: Seq[GoodsItemWithPic] = rowSeq.map{
            case (goods, None) => GoodsItemWithPic(GoodsItem(goods), "")
            case (goods, Some(pic)) => GoodsItemWithPic(GoodsItem(goods), pic.base64) }

          Ok( views.html.goods(loggedIn, goodsSeq, form) )
        }
      }

  def showitem(itemId: Int) = AsyncStack { implicit request =>
    database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map { row =>
      Ok( views.html.showitem(loggedIn, GoodsItem(row)) )
    }
  }

}

