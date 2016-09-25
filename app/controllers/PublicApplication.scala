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
import models.db.Tables.GoodsviewRow
import play.api.cache.CacheApi
import play.api.data.Form
import utils.db.TetraoPostgresDriver.api._
import play.api.i18n.Messages.Implicits._

import scala.concurrent.Future

@Singleton
class PublicApplication @Inject()(val database: DBService, val cache: CacheApi, implicit val webJarAssets: WebJarAssets,implicit  val dao: DAO)
  extends Controller with AuthConfigTrait with OptionalAuthElement {


  def index() = StackAction { implicit request =>
    Ok(views.html.index(loggedIn))
  }


  def goods(q: String, c: String, p: Int) = AsyncStack { implicit request =>
    val form: GoodsSearchFormData = searchGoodsForm.bindFromRequest.fold(
      withError => GoodsSearchFormData(q, c),   ok => ok
    )
    val qry = Tables.Goodsview.filter{ row =>
      val tmp = form.q.split(" ")
        .map { word => s"%${word.toLowerCase}%" }
        .map { word =>
          row.description.toLowerCase.like(word) ||
            row.title.toLowerCase.like(word) ||
            row.state.toLowerCase.like(word) ||
            row.codeid.toLowerCase.like(word) ||
            row.codes.toLowerCase.like(word) ||
            row.cars.toLowerCase.like(word) ||
            row.producedby.toLowerCase.like(word) ||
            row.trademark.toLowerCase.like(word)
        }
      tmp.tail.foldLeft(tmp.head)((prv, nxt)=> prv && nxt)
    }.filter { row => (row.category === form.cat.replaceAll(raw"(?i)Bce", "")) || (form.cat.isEmpty)  }
    val totalF: Future[Int] = database.runAsync(qry.length.result)
    val pageSize = 2
    val offest = pageSize * p
    val goodsF: Future[Seq[GoodsviewRow]] = database.runAsync(qry.sortBy(_.id).drop(offest).take(pageSize).result)
    for(total <- totalF; goodsviewRows <- goodsF)
      yield  Ok( views.html.goods(loggedIn, Page(goodsviewRows, p, offest, total), searchGoodsForm.fill(form)) )
  }


  def showitem(itemId: Int) = AsyncStack { implicit request =>
    database.runAsync(Tables.Goodsview.filter(_.id === itemId).result.head).map { goodsRow =>
      Ok( views.html.showitem(loggedIn, goodsRow) )
    }
  }


}

