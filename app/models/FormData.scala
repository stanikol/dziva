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

  def unapplyEntity(e:Entity[GoodsItem]): Option[(Int, GoodsItem)] =  Some((e.id, e.data))

  def applyEntity(id: Int, goodsItem: GoodsItem): Entity[GoodsItem] =
    Entity(id, new GoodsItem(goodsItem.price, goodsItem.qnt, goodsItem.category, goodsItem.title, goodsItem.description,
      goodsItem.producedby, goodsItem.trademark, goodsItem.cars, goodsItem.codeid, goodsItem.codes, goodsItem.state, goodsItem.pic) )


  val editGoodsItemForm = Form(mapping(
    "id"  ->  number,
    "goodsitem" ->
      mapping(
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
      )(GoodsItem.apply)(GoodsItem.unapply)
    )(applyEntity _ )(unapplyEntity _)
  )


  case class GoodsSearchFormData(q: String = "", cat: String = "")

  val searchGoodsForm = Form(
    mapping("q" -> text, "cat" -> text)
      (GoodsSearchFormData.apply)(GoodsSearchFormData.unapply)
  )



}
