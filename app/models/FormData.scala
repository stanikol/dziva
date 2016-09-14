package models

import play.api.data.{Mapping, Form}
import play.api.data.Forms._

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


//  "id"->"ID", "price"->"Цена",
//  "qnt"->"Кол", "producedBy"->"Производитель", "title"->"Наименование", "tradeMark"->"Торг. марка",
//  "description"->"Описание", "cars"->"Авто", "codeID"->"Код", "codes"->"Др. коды", "state"->"Состояние")
//  id: Int, price: scala.math.BigDecimal, qnt: Int, category: String,
// producedby: Option[String] = None, title: String, trademark: Option[String] = None, description: String, cars: Option[String] = None, codeid: Option[String] = None, codes: Option[String] = None, state: Option[String] = None
  val editGoodsItemForm = Form(
    mapping(
      "id"  -> number,
      "price" -> bigDecimal,
      "qnt" -> number,
      "category" -> text ,
      "producedby" -> optional(text),
      "title" -> text,
      "trademark" -> optional(text),
      "description" -> text,
      "cars" -> optional(text),
      "codeid" -> optional(text),
      "codes" -> optional(text),
      "state" -> optional(text)
    )(GoodsItem.apply)(GoodsItem.unapply)
  )
}
