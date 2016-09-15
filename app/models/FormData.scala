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

  implicit def goodsCategoriesFormatter: Formatter[db.GoodsCategories.Value] = new Formatter[db.GoodsCategories.Value] {
    def bind(key: String, data: Map[String, String]) = {
      try {
        Right({
          val id = db.GoodsCategories.values.find(_.toString == key).get.id
          db.GoodsCategories.apply(id)
        })
      } catch {
        case e: Exception => Left(List(FormError(key, "Your error message")))
      }
    }
    def unbind(key: String, value: db.GoodsCategories.Value) = Map(key -> value.toString)
  }
  val editGoodsItemForm = Form(
    mapping(
      "price"       -> bigDecimal,
      "qnt"         -> number,
      "category"    -> of[db.GoodsCategories.Value],
      "title"       -> text,
      "description" -> text,
      "producedby"  -> optional(text),
      "trademark"   -> optional(text),
      "cars"        -> optional(text),
      "codeid"      -> optional(text),
      "codes"       -> optional(text),
      "state"       -> optional(text),
      "pic"         -> optional(number)
    )(GoodsItem.apply)(GoodsItem.unapply)
  )
}
