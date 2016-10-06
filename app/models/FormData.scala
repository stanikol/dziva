package models

import java.text.SimpleDateFormat

import play.api.data.{Form, FormError, Mapping}
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.format.Formatter

case class FormDataLogin(email: String, password: String)

case class FormDataAccount(name:String, email: String, password: String, passwordAgain:String)

object FormData {

  val login = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(FormDataLogin.apply)(FormDataLogin.unapply)
  )

  val addMessage = Form(
    mapping(
      "content" -> nonEmptyText,
      "tags" -> text
    )(Message.formApply)(Message.formUnapply)
  )

  private[this] def accountForm(passwordMapping:Mapping[String]) = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email,
      "password" -> passwordMapping,
      "passwordAgain" -> passwordMapping
    )(FormDataAccount.apply)(FormDataAccount.unapply)
  )

  val updateAccount = accountForm(text)

  val addAccount = accountForm(nonEmptyText)

  implicit class ValidateTextLength(text: Mapping[String]) {
    def validateLength(maxLenght: Int) =
      text.verifying(s"Превышено максимальное количество символов, не более $maxLenght !", {_.length <= maxLenght })
  }

  val editGoodsItemForm = Form(mapping(
      "id"          ->  number,
      "price"       -> bigDecimal.verifying(s"Цена дложна быть больше нуля !", {_ >=0 }),
      "qnt"         -> number.verifying(s"Количество может целым, большим нуля !", {_ >=0 }),
      "category"    -> nonEmptyText.validateLength(20),
      "title"       -> nonEmptyText.validateLength(50),
      "description" -> nonEmptyText.validateLength(255),
      "producedby"  -> optional(text.validateLength(255)),
      "trademark"   -> optional(text.validateLength(50)),
      "cars"        -> optional(text.validateLength(255)),
      "codeid"      -> optional(text.validateLength(50)),
      "codes"       -> optional(text.validateLength(255)),
      "state"       -> optional(text.validateLength(20)),
      "pic"         -> optional(number)
    )(db.Tables.GoodsRow.apply)(db.Tables.GoodsRow.unapply)
  )

  val searchGoodsForm = Form(
    mapping("q" -> text, "c" -> text)
      (GoodsSearchFormData.apply)(GoodsSearchFormData.unapply)
  )

  val editItemPhotoForm = Form(mapping(
  "id"              ->  optional(number),
  "action_delete"   ->  optional(text),
  "action_select"   ->  optional(text),
  "action_newfile"  ->  optional(text),
  "action_rename"   ->  optional(text),
  "q"               ->  optional(text),
  "picid"           ->  optional(number)
  )(EditItemPhoto.apply)(EditItemPhoto.unapply))
}

case class GoodsSearchFormData(q: String, cat: String)

case class EditItemPhoto(id: Option[Int], action_delete: Option[String], action_select: Option[String],
                          action_newfile: Option[String], action_rename: Option[String], q: Option[String], picid: Option[Int])

//case class PageOfPics(pics: Seq[db.Tables.SmallPicsRow], page: Int, total: Int)

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}
