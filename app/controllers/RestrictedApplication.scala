package controllers

import java.time.OffsetDateTime
import javax.inject.{Inject, Singleton}

import com.github.t3hnar.bcrypt.Password
import jp.t2v.lab.play2.auth.AuthElement
import models.db.Tables.GoodsRow
import models.db.{AccountRole, Tables}
import models._
import play.api.Logger
import play.api.mvc.Controller
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RestrictedApplication @Inject()(val database: DBService, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with AuthElement {
  import play.api.i18n.Messages.Implicits._

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
    implicit val goodsCategoriesTypeMapper = MappedColumnType.base[models.db.GoodsCategories.Value, String](
      { g => g.toString },    //
      { s => models.db.GoodsCategories.withName(s) }
    )

    val actionParam = request.getQueryString("action")
    Logger.debug(s"RestrictedApplication.editform($itemId): action == ${actionParam}")
    val supportedActions = Seq("Сохранить", "Удалить", "Новый")
    actionParam match {
      case Some(action) if supportedActions.contains(action) =>
        action match {
              case "Удалить" =>
                database.runAsync(Tables.Goods.filter(_.id === itemId).delete).map(id => Redirect(routes.PublicApplication.goods()))
              case "Сохранить" => Future.successful {
                Logger.error(s"Сохранить $itemId")
                FormData.editGoodsItemForm.bindFromRequest().fold(
                  { withError =>
                    Logger.error(s"Ошибка чтения данных формы ${withError.hasErrors}\n")
                    Logger.error(s"Данные формы  data=${withError.data}\n")
                    BadRequest(views.html.edititem(loggedIn, withError)) },
                  { goodsItemEntity  =>
                    Logger.info(s"Форма прочитана нормально id=${goodsItemEntity.id}")
                    val q = for {row <- Tables.Goods if row.id === itemId}
                      yield (row.id, row.price, row.qnt, row.category, row.title, row.description,
                        row.producedby, row.trademark, row.cars, row.codeid, row.codes, row.state, row.pic)
                    val updateQuery = q.update(Tables.GoodsRow.unapply(goodsItemEntity).get)
                      database.runAsync( updateQuery )
                    Ok(views.html.edititem(loggedIn, FormData.editGoodsItemForm.fill(goodsItemEntity)))
//                    Redirect(routes.RestrictedApplication.edititem(itemId))
                  }
                )
              }
              case "Новый" =>
                val newItem = GoodsRow(-1, 0, 0, "Компрессора", "", "", None, None, None, None, None, None, Some(1))
                database.runAsync((Tables.Goods returning Tables.Goods.map(_.id)) +=  newItem).map{ id =>
                  Redirect(routes.RestrictedApplication.edititem(id))
                }
        }
      case Some(action) =>
        Logger.error(s"RestrictedApplication.editform($itemId): Неизвестный action == $action")
        Future.successful(Ok(s"Неизвестный action == $action"))
      case None =>
        Logger.info(s"edititem: no action specified")
        database.runAsync(Tables.Goods.filter(_.id === itemId).result.head).map{ goodsRow =>
          val form = FormData.editGoodsItemForm.fill(goodsRow)
          Logger.info(s"withError => data=${form.data}\n")

          Ok(views.html.edititem(loggedIn, form))
        }
    }
  }
}


