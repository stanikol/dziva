package controllers

import java.time.OffsetDateTime
import javax.inject.{Inject, Singleton}

import com.github.t3hnar.bcrypt.Password
import jp.t2v.lab.play2.auth.AuthElement
import models.db.Tables.GoodsRow
import models.db.{AccountRole, Tables}
import models._
import play.api.Logger
import play.api.data.Form
import play.api.mvc.Controller
import play.twirl.api._
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RestrictedApplication @Inject()(val database: DBService, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with AuthElement {

  def messages() = AsyncStack(AuthorityKey -> AccountRole.normal) { implicit request =>
    database.runAsync(Tables.Message.sortBy(_.id).result).map { rowSeq =>
      val messageSeq = rowSeq.map(Message(_))
      Ok(views.html.messages(loggedIn, messageSeq, FormData.addMessage))
    }
  }

  def deleteMessage(id:Int) = AsyncStack(AuthorityKey -> AccountRole.normal) { implicit request =>
    database.runAsync(Tables.Message.filter(_.id === id).delete).map {_ =>
      Logger.info(s"Deleted message#$id by ${loggedIn.data.email}" )
      Redirect(routes.RestrictedApplication.messages())
    }
  }

  def addMessage() = AsyncStack(AuthorityKey -> AccountRole.normal) { implicit request =>
    FormData.addMessage.bindFromRequest.fold(
      formWithErrors => Future.successful(Redirect(routes.RestrictedApplication.messages())),
      message => {
        database.runAsync((Tables.Message returning Tables.Message.map(_.id)) += message.toRow()).map { id =>
          Logger.info(s"Inserted message#$id by ${loggedIn.data.email}")
          Redirect(routes.RestrictedApplication.messages())
        }
      }
    )
  }

  def account() = StackAction(AuthorityKey -> AccountRole.normal) { implicit request =>
    val form = FormData.updateAccount.fill(FormDataAccount(loggedIn.data.name, loggedIn.data.email, "", ""))
    Ok(views.html.account(loggedIn, form, insert = false))
  }

  def updateAccount() = AsyncStack(AuthorityKey -> AccountRole.normal) { implicit request =>
    FormData.updateAccount.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.account(loggedIn, formWithErrors, insert = false))),
      accountFormData => {
        if(accountFormData.password.nonEmpty && accountFormData.password != accountFormData.passwordAgain) {
          val form = FormData.addAccount.fill(accountFormData).withError("passwordAgain", "Passwords don't match")
          Future.successful(BadRequest(views.html.account(loggedIn, form, insert = false)))
        } else {
          val now = OffsetDateTime.now()
          val update = if(accountFormData.password.nonEmpty) {
            //update also password
            val bcryptedPassword = accountFormData.password.bcrypt
            val q = for {
              row <- Tables.Account if row.id === loggedIn.id
            } yield (row.name, row.email, row.password, row.updatedAt)
            q.update((accountFormData.name, accountFormData.email, bcryptedPassword, now))
          } else {
            val q = for {
              row <- Tables.Account if row.id === loggedIn.id
            } yield (row.name, row.email, row.updatedAt)
            q.update((accountFormData.name, accountFormData.email, now))
          }
          database.runAsync(update).map { _ =>
            Logger.info(s"Updated account of ${loggedIn.data.email}")
            Redirect(routes.RestrictedApplication.messages())
          }
        }
      }
    )
  }

  def newAccount() = StackAction(AuthorityKey -> AccountRole.admin) { implicit request =>
    Ok(views.html.account(loggedIn, FormData.addAccount, insert = true))
  }

  def addAccount() = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>
    FormData.addAccount.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.account(loggedIn, formWithErrors, insert = true))),
      accountFormData => {
        if(accountFormData.password.nonEmpty && accountFormData.password == accountFormData.passwordAgain) {
          val now = OffsetDateTime.now()
          val row = Tables.AccountRow(
            id = -1,
            name = accountFormData.name,
            email = accountFormData.email,
            password = accountFormData.password.bcrypt,
            role = AccountRole.normal,
            updatedAt = now,
            createdAt = now
          )
          database.runAsync((Tables.Account returning Tables.Account.map(_.id)) += row).map { id =>
            Logger.info(s"Inserted account#$id by ${loggedIn.data.email}")
            Redirect(routes.RestrictedApplication.messages())
          }
        } else {
          val form = FormData.addAccount.fill(accountFormData).withError("passwordAgain", "Passwords don't match")
          Future.successful(BadRequest(views.html.account(loggedIn, form, insert = true)))
        }
      }
    )
  }

  def edititem(itemId: Int) = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>
    FormData.editGoodsItemForm.bindFromRequest.fold(
      {errorForm =>
        database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map{ row =>
          var goodsitem = GoodsItem(row)
          val item4sale = GoodsItem4Sale(GoodsItem(row), "")
          BadRequest(views.html.edititem(loggedIn, item4sale))
        }},
//        Future.successful(BadRequest(views.html.edititem(loggedIn, itemId, errorForm))),
      { ok =>
        request.getQueryString("action") match {
          case Some(param) if param == "Удалить" =>
            Logger.info(s"удалить $itemId")
            database.runAsync(Tables.Goods.filter(_.id === itemId).delete).map{ x =>
              Redirect(routes.PublicApplication.goods())
            }

          case Some(param) if param == "Новый" =>
            val emptyItem = Tables.GoodsRow(
                id = -1,
                price = 0,
                qnt = 0,
                category = models.db.GoodsCategories.Разное,
                producedby = Some(""),
                title = "",
                trademark = Some(""),
                description = "",
                cars = Some(""),
                codeid = Some(""),
                codes = Some(""),
                state = Some("")
            )
            database.runAsync((Tables.Goods returning Tables.Goods.map(_.id)) += emptyItem).map { id =>
              Redirect(routes.RestrictedApplication.edititem(id))
            }
//          case Some(param) if param == "Сохранить" =>
//            Logger.info(s"сохранить $itemId")
//            val updatedItem = new GoodsItem(
//              ok.price,
//              ok.qnt,
//              ok.category,
//              ok.title,
//              ok.description,
//              ok.producedby,
//              ok.trademark,
//              ok.cars,
//              ok.codeid,
//              ok.codes,
//              ok.state,
//              ok.pic
//            )
//            val q = for {
//              row <- Tables.Goods if row.id === itemId
//            } yield (row.price, row.qnt, row.category, row.title, row.description,
//                      row.producedby, row.trademark, row.cars, row.codeid, row.codes, row.state, row.pic)
//
////            database.runAsync(q.update(GoodsItem.unapply(updatedItem).get))
//            database.runAsync(q.update((
//              ok.price,
//              ok.qnt,
//              ok.category,
//              ok.title,
//              ok.description,
//              ok.producedby,
//              ok.trademark,
//              ok.cars,
//              ok.codeid,
//              ok.codes,
//              ok.state,
//              ok.pic)))
//
//            database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map{ row =>
//              var goodsitem = GoodsItem(row)
//              Ok(views.html.edititem(loggedIn, itemId, FormData.editGoodsItemForm.fill(goodsitem.data)))
//            }
          case _ =>
            Logger.error("HHHHHHHHHH")
            database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map{ row =>
              val item4sale = GoodsItem4Sale(GoodsItem(row), "")
              Ok(views.html.edititem(loggedIn, item4sale))
            }
        }
      }
    )

  }
}


