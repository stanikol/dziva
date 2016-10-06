package controllers

import java.io.{FileInputStream, FileOutputStream}
import java.time.OffsetDateTime
import javax.annotation.Resource
import javax.inject.{Inject, Singleton}

import com.github.t3hnar.bcrypt.Password
import jp.t2v.lab.play2.auth.AuthElement
import models.db.Tables.{GoodsCategoryRow, GoodsRow, GoodsviewRow, SmallPicsRow}
import models.db.{AccountRole, Tables}
import models._
import play.api.{Logger, Play}
import play.api.mvc._
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import helpers.Hlp._
import models.FormData._
import java.io.File

import jp.t2v.lab.play2.stackc.RequestWithAttributes
import play.api.cache.CacheApi
import sun.misc.BASE64Encoder
import views.html
import play.api.data.Forms._
import play.api.data.Form
import play.twirl.api.Html

import scala.util.{Failure, Success}

@Singleton
class DzivaApp @Inject()(val database: DBService, val cache: CacheApi, implicit val webJarAssets: WebJarAssets, implicit val dao: DAO)
  extends Controller with AuthConfigTrait with AuthElement {
  import play.api.i18n.Messages.Implicits._


  def newitem = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>
    val newItem = GoodsRow(-1, 0, 0, "Компрессора", "", "", None, None, None, None, None, None, None)
    database.runAsync((Tables.Goods returning Tables.Goods.map(_.id)) +=  newItem).map{ id =>
      Redirect(routes.DzivaApp.edititem(id))
    }
  }


  def edititem(itemId: Int) = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>
    val action: Option[String] = play.api.data.Form(single("action"->optional(text))).bindFromRequest.fold(error => None, act => act)
    val actionMessage = s"Редактировать товар($itemId): action == ${action}"
    Logger.info(actionMessage)
    action match {
      case Some("Удалить") =>
        Logger.info(s"Удалить $itemId")
        database.runAsync(Tables.Goods.filter(_.id === itemId).delete).map(id =>
          Redirect(routes.PublicApplication.goods())
        )
      case Some("Сохранить") =>
        val msg = s"Сохранить $itemId"
        Logger.info(msg)
        editGoodsItemForm.bindFromRequest().fold(
          { withError =>
            Logger.error(msg+": "+s"Ошибка чтения данных формы ${withError.hasErrors}\n")
            Logger.error(msg+": "+s"Данные формы  data=${withError.data}\n")
            Future.successful(BadRequest(views.html.edititem(loggedIn, withError))) },
          { goodsItemEntity  =>
            Logger.info(msg+": "+s"Форма прочитана нормально id=${goodsItemEntity.id}")
            val insertOrUpdateQuery = Tables.Goods.insertOrUpdate(goodsItemEntity)
            database.runAsync(insertOrUpdateQuery).map( _ =>
              Redirect(routes.DzivaApp.edititem(itemId))
            )
          }
        )
      case Some(unknown_action) =>
        Logger.error(s"Неизвестный action == $unknown_action!")
        Future.successful(Ok(s"ОШИБКА: Неизвестный action == $unknown_action"))
      case None =>
        Logger.info(actionMessage+": "+s"Показать форму")
        database.runAsync(Tables.Goodsview.filter(_.id === itemId).result.head).map{ goodsviewRow =>
          val form = editGoodsItemForm.fill(goodsviewRow)
          Logger.info(s"withError => data=${form.data}\n")
          Ok(views.html.edititem(loggedIn, form, Some(goodsviewRow.base64.getOrElse("NO PHOTO"))))
        }
    }
//    Future.successful(NotImplemented("Fuck them all !"))
  }

  /**
    *
    * @param id
    * @param searchPhotoStr
    * @param page
    * @return
    */
  def edititemphoto(id:             Option[Int],
                    searchPhotoStr: Option[String],
                    page:    Int) = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>

    //
    val resultF: Future[Either[String, String]] = editItemPhotoForm.bindFromRequest().fold(
        { error          =>
          Logger.error("edititemphoto:=> No action will be peformed !" + "ERROR -> Ошибка при чтении параметров!")
          Future.successful(Left("ERROR -> Ошибка при чтении параметров!")) },
        { editItemPhoto =>
            editItemPhoto match {
              // Установить картинку для товра из выбраного в picid
              case EditItemPhoto(Some(id), None, Some(action_select), None, None, _, Some(newPicID)) =>
                Logger.info(s"Установить картинку newPicID==$newPicID для товара id==$id")
                val qry = for (g <- Tables.Goods if g.id === id) yield g.pic
                database.runAsync(qry.update(Some(newPicID))).map { _ =>
                  Right(s"Для товра id==$id установлена картинка newPicID==$newPicID")
                } .recover{ case error: Throwable => Left(error.getMessage) }
              // Установить картинку для товра из загружаемого пользователем файла
              case EditItemPhoto(Some(id), None, None, Some(action_newfile), None, _, _) =>
                Logger.info(s"Загрузить новый файл для товара id==$id")
                getFileFromRequestAndSaveIt(request).flatMap({
                  case Left(errorMessage) => Future.successful(Left(errorMessage))
                  case Right(newPicID)    =>
                    val qry = for (g <- Tables.Goods if g.id === id) yield g.pic
                    database.runAsync(qry.update(Some(newPicID))).map { _ =>
                      Right(s"Картинка для товара $id обновлена на $newPicID")
                    } .recover{ case error: Throwable => Left(error.getMessage) }
                })
              // Загрузить новую картинку. Товар не выбран !
              case EditItemPhoto(None, None, None, Some(action_newfile), None, _, None) =>
                Logger.info("Загрузить новую картинку в БД.")
                getFileFromRequestAndSaveIt(request) map {
                  case Left(errorMsg)  => Left(errorMsg)
                  case Right(newPicID) => Right(s"В БД успешно загружена новая картинка с id=$id.")
                }
              // Удалить картинку.
              case EditItemPhoto(_, Some(action_delete), None, None, None, _, Some(picid)) =>
                database.runAsync(Tables.SmallPics.filter(_.id === picid).delete)
                  .map { _ =>Right(s" Картинка  успешно удалена.") }
                    .recover{ case error: Throwable => Left(error.getMessage) }
              // Переименовать картинку.
              case EditItemPhoto(_, None, None, None, Some(action_rename), Some(newname), Some(picid)) =>
                val qry = for(p <- Tables.SmallPics if p.id === picid) yield p.name
                database.runAsync(qry.update(newname))
                  .map { _ =>Right(s" Картинка  успешно переименована в $newname.") }
                  .recover{ case error: Throwable => Left(error.getMessage) }
              case other =>
                Logger.error("По-умолчанию")
                Future.successful(Right(""))
            }
          }
    )
    val searchStr: String = Form(single("q"->text)).bindFromRequest().fold(
      error => searchPhotoStr.getOrElse("%"),
      q => s"%${q}%"
    )
    val pageSize = 32
    val offset = pageSize * page
    val picsF: Future[Seq[Tables.SmallPicsRow]] =
      database.runAsync(Tables.SmallPics.filter(_.name.toLowerCase.like(searchStr)).drop(offset).take(pageSize).result
    )
    val picsLengthF: Future[Int] = database.runAsync(
      Tables.SmallPics.filter(_.name.toLowerCase.like(searchStr)).length.result
    )
    val goodsviewRowFO: Future[Option[Tables.GoodsviewRow]] =
      if(id.isDefined)  database.runAsync(Tables.Goodsview.filter(_.id === id).result.headOption)
      else              Future.successful(None)
    for ( r <- resultF;
          p <- picsF;
          g <- goodsviewRowFO;
          t <- picsLengthF)
      yield Ok(views.html.edititemphoto(loggedIn, g, Some(Page(p, page, offset, t)), r))
  }

  case class CategoryForm(delcat: Option[String], delcatact: Option[String],
                          newcat: Option[String], newcatact: Option[String])
  val categoryForm: Form[CategoryForm] = Form(mapping(
    "delcat"      ->  optional(text),
    "delcatact"   ->  optional(text),
    "newcat"      ->  optional(text),
    "newcatact"   ->  optional(text)
  )(CategoryForm.apply)(CategoryForm.unapply))

  def editCategories = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>
    categoryForm.bindFromRequest().fold(
      { errorForm        =>  Future.successful(BadRequest("Bad request")) },
      { categoryFormData =>
          val resultF: Future[Option[Either[String, String]]] =
            categoryFormData match {
              case CategoryForm(Some(delcat), Some(delcatact), None, None) =>
                val canDeleteCategoryQuery = Tables.Goods.filter(_.category === delcat).length.result
                dao.database.runAsync(canDeleteCategoryQuery).flatMap { numberOfRowsFound =>
                  if(numberOfRowsFound>0)
                    Future.successful(Some(Left(
                      s"Категория '$delcat' не может быть удалена. В БД найдено $numberOfRowsFound товаров использующих категорию $delcat.")))
                  else {
                    val dropCategoryQuery = Tables.GoodsCategory.filter(_.name === delcat).delete
                    dao.database.runAsync(dropCategoryQuery).map { _ =>
                      cache.remove("categories")
                      Some(Right(s"Категория '$delcat' успешно удалена"))
                    }
                  }
                }
              case CategoryForm(None, None, Some(newcat), Some(newcatact)) =>
                val canAddNewCategoryQuery = Tables.GoodsCategory.filter(_.name === newcat).length.result
                dao.database.runAsync(canAddNewCategoryQuery).flatMap { numberOfRowsFound =>
                  if(numberOfRowsFound>0)
                    Future.successful(Some(Left(s"Категория '$newcat' уже существует.")))
                  else {
                    val addNewCategoryQuery = Tables.GoodsCategory += GoodsCategoryRow(-1, newcat)
                    dao.database.runAsync(addNewCategoryQuery).map { _ =>
                      cache.remove("categories")
                      Some(Right(s"Категория '$newcat' успешно добавлена."))
                    }
                  }
                }
              case CategoryForm(None, None, None, None) => Future.successful(None)
              case unknown =>
                Future.successful(Some(Left(s"Инвалидные данные '$unknown'.")))
          }
        resultF.map( result => (Ok(html.editCategories(loggedIn, result))) )
      }
    )

  }//def editCategories = AsyncStack(AuthorityKey -> AccountRole.admin) { implicit request =>

  private def getFileFromRequestAndSaveIt( req: Request[AnyContent]) : Future[Either[String, Int]] = {
    req.body.asMultipartFormData.get.file("newfile").map { newPic =>
      val filename = newPic.filename.substring(newPic.filename.lastIndexOf(File.pathSeparator) + 1).takeRight(40)
      val contetType = newPic.contentType
      val is = new FileInputStream(newPic.ref.file)
      val loggerMessage = s"Попытка сохранениея в БД $contetType файла $filename."
      Logger.info(loggerMessage)
      val byte: Array[Byte] = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
      if (byte.length == 0) {
        val errorMessage = s"ERROR -> Файл $filename не найден или не загружен."
        Logger.info(loggerMessage +": "+ errorMessage)
        Future.successful(Left(errorMessage))
      } else {
        val base64: String = new BASE64Encoder().encode(byte)
        Logger.info(s"Длина нового файла $filename в base64 = ${base64.length}")
        val pic = SmallPicsRow(-1, filename, s"data:$contetType;charset=utf-8;base64," + base64)
        database.runAsync((Tables.SmallPics.returning(Tables.SmallPics.map(_.id))) += pic).map(id => Right(id))
      }
    }.getOrElse{
      val errorMessage = "ERROR -> Ошибка чтения данных формы. Файл не загружен!"
      Logger.error(errorMessage)
      Future.successful(Left(errorMessage))
    }
  }

}


