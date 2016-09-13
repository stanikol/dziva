package models

import java.time.OffsetDateTime

import models.db.{AccountRole, Tables}

import scala.collection.immutable.ListMap

case class Entity[T](id:Int, data:T)

case class Account(name: String, email: String, role: AccountRole.Value) {
  def isAdmin: Boolean = role == AccountRole.admin
}

object Account {
  def apply(row: Tables.AccountRow): Entity[Account] = {
    Entity(
      id = row.id,
      data = Account(
        name = row.name,
        email = row.email,
        role = row.role
      )
    )
  }
}

case class Message(content:String, tagSet:Set[String]) {
  def toRow() = {
    val now = OffsetDateTime.now()
    Tables.MessageRow(
      id = -1,
      content = content,
      tagList = tagSet.toList,
      createdAt = now,
      updatedAt = now
    )
  }
}

object Message {
  def apply(row: Tables.MessageRow): Entity[Message] = {
    Entity(
      id = row.id,
      data = Message(
        content = row.content,
        tagSet = row.tagList.toSet
      )
    )
  }

  def formApply(content:String, tags:String):Message = {
    Message(
      content = content.trim,
      tagSet = tags.split(",").map(_.trim).filterNot(_.isEmpty).toSet
    )
  }

  def formUnapply(m:Message):Option[(String, String)] = {
    Some((m.content, m.tagSet.mkString(",")))
  }
}



  case class GoodsItem(id: Int, price: scala.math.BigDecimal, qnt: Int, category: String, producedby: Option[String] = None,
                       title: String, trademark: Option[String] = None, description: String, cars: Option[String] = None,
                       codeid: Option[String] = None, codes: Option[String] = None, state: Option[String] = None){
//  case class GoodsItem(id: Int, price: BigDecimal, qnt: Int, category: String, title: String,
//                       producedBy: Option[String], tradeMark: Option[String], description: String,
//                       cars: Option[String], codeID: Option[String],
//                       codes: Option[String], state: Option[String]){
    def asMap = CaseClassToMap.apply(this)

    object CaseClassToMap {
      def apply(cc: AnyRef) =
        (ListMap[String, String]() /: cc.getClass.getDeclaredFields) {(a, f) =>
          f.setAccessible(true)
          a + (f.getName -> {f.get(cc) match {case Some(x) => x.toString case None => "" case x => x.toString}})
        }
    }
  }

  object GoodsItem {
    val header: ListMap[String, String] = ListMap("id"->"ID", "price"->"Цена",
      "qnt"->"Кол", "category"->"Категория", "producedby"->"Производитель", "title"->"Наименование", "trademark"->"Торг. марка",
      "description"->"Описание", "cars"->"Авто", "codeid"->"Код", "codes"->"Др. коды", "state"->"Состояние")

    def apply(row: Tables.GoodsRow): Entity[GoodsItem] =
      Entity(
        id = row.id,
        data = new GoodsItem(
          id = row.id,
          price = row.price,
          qnt = row.qnt,
          category = row.category,
          title = row.title,
          producedby = row.producedby,
          trademark = row.trademark,
          description = row.description,
          cars = row.cars,
          codeid = row.codeid,
          codes = row.codes,
          state = row.state
        )
      )

  }



