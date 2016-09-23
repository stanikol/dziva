package models

import java.text.SimpleDateFormat

import models.db.GoodsCategories
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
        Right( db.GoodsCategories.withName(key) )
      } catch {
        case e: Exception => Left(List(FormError(key, "Your error message")))
      }
    }
    def unbind(key: String, value: db.GoodsCategories.Value) = Map(key -> value.toString)
  }

// id: Int, price: scala.math.BigDecimal, qnt: Int, category: String, title: String,
//  description: String, producedby: Option[String] = None, trademark: Option[String] = None, cars: Option[String] = None,
// codeid: Option[String] = None, codes: Option[String] = None, state: Option[String] = None, pic: Option[Int] = None)
  val editGoodsItemForm = Form(mapping(
    "id"  ->  number,
            "price"       -> bigDecimal,
            "qnt"         -> number,
            "category"    -> text,
            "title"       -> text,
            "description" -> text,
            "producedby"  -> optional(text),
            "trademark"   -> optional(text),
            "cars"        -> optional(text),
            "codeid"      -> optional(text),
            "codes"       -> optional(text),
            "state"       -> optional(text),
            "pic"         -> optional(number)
    )(db.Tables.GoodsRow.apply)(db.Tables.GoodsRow.unapply)
  )


  case class GoodsSearchFormData(q: String = "", cat: String = "")

  val searchGoodsForm = Form(
    mapping("q" -> text, "cat" -> text)
      (GoodsSearchFormData.apply)(GoodsSearchFormData.unapply)
  )




}
case class PageOfPics(pics: Seq[db.Tables.SmallPicsRow], page: Int, total: Int)
