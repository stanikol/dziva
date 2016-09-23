package controllers

import java.io.{FileInputStream, FileOutputStream}
import java.time.OffsetDateTime
import javax.annotation.Resource
import javax.inject.{Inject, Singleton}

import apple.laf.JRSUIUtils.Images
import com.github.t3hnar.bcrypt.Password
import jp.t2v.lab.play2.auth.AuthElement
import models.db.Tables.{GoodsRow, SmallPicsRow}
import models.db.{AccountRole, Tables}
import models._
import play.api.{Logger, Play}
import play.api.mvc.Controller
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import helpers.Hlp._
import models.PageOfPics
import play.api.data.Form
import sun.misc.BASE64Encoder
import views.html

import scala.util.Success

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
    val actionParam = request.getQueryString("action")
    Logger.debug(s"RestrictedApplication.editform($itemId): action == ${actionParam}")
    val supportedActions = Seq("Сохранить", "Удалить", "Новый")
    actionParam match {
      case Some(action) if supportedActions.contains(action) =>
        action match {
              case "Удалить" =>
                database.runAsync(Tables.Goods.filter(_.id === itemId).delete).map(id => Redirect(routes.PublicApplication.goods()))
              case "Сохранить" =>
                Logger.error(s"Сохранить $itemId")
                FormData.editGoodsItemForm.bindFromRequest().fold(
                  { withError =>
                    Logger.error(s"Ошибка чтения данных формы ${withError.hasErrors}\n")
                    Logger.error(s"Данные формы  data=${withError.data}\n")
                    Future.successful(BadRequest(views.html.edititem(loggedIn, withError))) },
                  { goodsItemEntity  =>
                    Logger.info(s"Форма прочитана нормально id=${goodsItemEntity.id}")
                    val insertOrUpdateQuery = Tables.Goods.insertOrUpdate(goodsItemEntity)
                    database.runAsync(insertOrUpdateQuery).map( _ =>
                      Redirect(routes.RestrictedApplication.edititem(itemId))
                    )
                  }
                )
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
        database.runAsync(Tables.Goodsview.filter(_.id === itemId).result.head).map{ goodsviewRow =>
        val form = FormData.editGoodsItemForm.fill(goodsviewRow)
          Logger.info(s"withError => data=${form.data}\n")
          Ok(views.html.edititem(loggedIn, form, Some(goodsviewRow.base64.getOrElse("NO PHOTO"))))
        }
    }
  }

  /**
    *
    * @param id
    * @param searchPhotoStr
    * @param currentPage
    * @param totalPages
    * @return
    */
  def edititemphoto(id:         Int,
                    searchPhotoStr: Option[String] = None,
                    currentPage:    Option[Int] = None,
                    totalPages:     Option[Int] = None) = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>

    val getCurrentItem = Tables.Goodsview.filter(_.id === id).result.headOption
    val getSmallPics = Tables.SmallPics.filter(_.name.toLowerCase.like(s"%${searchPhotoStr.getOrElse("").toLowerCase}%"))

    case class EditItemPhoto(id: Int, action: Option[String], q: Option[String], picid: Option[Int])
    import play.api.data.Forms._
    import play.api.data.Form
    val editItemPhotoForm = Form(mapping(
      "id"      ->  number,
      "action"  ->  optional(text),
      "q"       ->  optional(text),
      "picid"   ->  optional(number)
    )(EditItemPhoto.apply)(EditItemPhoto.unapply))

    editItemPhotoForm.bindFromRequest().fold(
      { error => Future.successful(BadRequest(s"Ошибка данных формы. ${error.errors.map(_.message)}")) },
      { editItemPhoto =>
          val goodsviewRows: Future[Tables.GoodsviewRow] = database.runAsync(Tables.Goodsview.filter(_.id === id).result.head)
          val pics: Future[Seq[Tables.SmallPicsRow]] = database.runAsync(
              Tables.SmallPics.filter(_.name.toLowerCase.like(s"%${editItemPhoto.q.getOrElse("")}%")).result
          )
          editItemPhoto match {
            case EditItemPhoto(id, Some(action), q, None)  =>
              request.body.asMultipartFormData.get.file("newfile").map { newPic =>
                import java.io.File
                val filename = newPic.filename.substring(newPic.filename.lastIndexOf(File.pathSeparator)+1)
                val contetType = newPic.contentType
                val is = new FileInputStream(newPic.ref.file)
                val byte: Array[Byte] = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
                val base64: String = new BASE64Encoder().encode(byte)
                Logger.info(s"Длина нового файла в base64 = ${base64.length}")
                val pic = SmallPicsRow(-1, filename, s"data:$contetType;charset=utf-8;base64," + base64)
                database.runAsync(Tables.SmallPics.returning(Tables.SmallPics.map(_.id)) += pic).flatMap { newPicID =>
                  database.runAsync {
                    val qry = for (g <- Tables.Goods if g.id === id) yield (g.pic)
                    qry.update((Some(newPicID)))
                  }.map { _ =>
                    Redirect(routes.RestrictedApplication.edititemphoto(id = id))
                  }
                }
              }.getOrElse(Future.successful(BadRequest(" Файл не выбнан")))

            case EditItemPhoto(id, Some(_), _, picOpt @ Some(_:Int) )  =>
              val updatePicQry = for(i <- Tables.Goods if i.id === id) yield i.pic
              database.runAsync(updatePicQry.update(picOpt)).map (_ => Redirect(routes.RestrictedApplication.edititem(id)))
            case _ =>
              (for( g <- goodsviewRows; p <- pics)
                yield  Ok(views.html.edititemphoto(loggedIn, g, Some(PageOfPics(p, 1 , 2))))
              ).recover{ case exception => BadRequest(s"Ошибка ${exception.getMessage}") }
          }
      }
    )
//    Logger.error(s"asFormUrlEncoded = ${request.body.asFormUrlEncoded.get}")
//    Future.successful(Ok(s"params = ${request.asMap}"))
//    request.getQueryString("action") match {
//      case Some(action) if action == "Сохранить загруженный"=>
//          Logger.error("Saving FILE")
//          request.body.asMultipartFormData.get.file("file").map { newPic =>
//            import java.io.File
//            val filename = newPic.filename.takeRight(19)
//            val contetType = newPic.contentType
//            val is = new FileInputStream(newPic.ref.file)
//            val byte: Array[Byte] = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
//            val base64: String = new BASE64Encoder().encode(byte)
//            val pic = SmallPicsRow(-1, filename, s"data:$contetType;charset=utf-8;base64," + base64)
//            database.runAsync(Tables.SmallPics.returning(Tables.SmallPics.map(_.id)) += pic).flatMap { newPicID =>
//              database.runAsync {
//                val qry = for (g <- Tables.Goods if g.id === id) yield (g.pic)
//                qry.update((Some(newPicID)))
//              }.map { _ =>
//                Redirect(routes.RestrictedApplication.edititemphoto(id = id))
//              }
//            }
//          }.getOrElse(Future.successful(BadRequest(" Файл не выбнан")))
//      case Some(action) if action == "Выбрать" && request.getQueryString("pic").isDefined =>
//
////        Future.successful(Redirect(routes.RestrictedApplication.edititemphoto(id = id)))
//        Future.successful(Ok(s"You choseg ${request.getQueryString("pic").get}"))
//      case _ =>
//        Logger.error("No Action")
//        database.runAsync(getCurrentItem).flatMap({
//          case Some(goodsviewItem) if Seq(searchPhotoStr, currentPage, totalPages).forall(_.nonEmpty) &&
//            currentPage.get <= totalPages.get =>
//            database.runAsync(getSmallPics.drop(10*currentPage.get).take(10).result).map { smallpicsRow =>
//              Ok(html.edititemphoto(loggedIn, goodsviewItem, Some(PageOfPics(smallpicsRow, currentPage.get, totalPages.get))))
//            }
//          case Some(goodsviewItem) if searchPhotoStr.isDefined && Seq(currentPage, totalPages).forall(_.isEmpty) =>
//            database.runAsync(getSmallPics.result).map { smallpicsRow =>
//              Ok(html.edititemphoto(loggedIn, goodsviewItem,
//                Some(PageOfPics(smallpicsRow.take(10), 1, Math.ceil( totalPages.get.toDouble / smallpicsRow.length ).toInt))
//              ))
//            }
//          case Some(goodsviewItem) if  Seq(searchPhotoStr, currentPage, totalPages).forall(_.isEmpty) =>
//            database.runAsync(getSmallPics.result).map { smallpicsRow =>
//              Ok(html.edititemphoto(loggedIn, goodsviewItem,
//                Some(PageOfPics(smallpicsRow.take(10), 1,1))
//              ))
//            }
//          case None =>
//            Future.successful(BadRequest(s"No such item $id"))
//        })
//    }


  }






}


