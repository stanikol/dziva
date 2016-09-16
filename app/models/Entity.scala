package models

import java.time.OffsetDateTime

import models.db.{AccountRole, Tables, GoodsCategories}

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


//--- id, price , qnt,
//--- category, title,
//--- description ,
//--- producedby, trademark , cars,
//--- codeid, codes,
//--- state, pic

case class GoodsItem(price: scala.math.BigDecimal, qnt: Int,
                     category: GoodsCategories.Value, title: String,
                     description: String,
                     producedby: Option[String] = None, trademark: Option[String] = None,  cars: Option[String] = None,
                     codeid: Option[String] = None, codes: Option[String] = None,
                     state: Option[String] = None, pic: Option[Int]){
  def asMap = {
    object CaseClassToMap {
      def apply(cc: AnyRef) =
        (ListMap[String, String]() /: cc.getClass.getDeclaredFields) {(a, f) =>
          f.setAccessible(true)
          a + (f.getName -> {f.get(cc) match {case Some(x) => x.toString case None => "" case x => x.toString}})
        }
    }
    CaseClassToMap.apply(this)
  }
}


object GoodsItem {
  val header: ListMap[String, String] = ListMap(
    "price"->"Цена", "qnt"->"Кол",
    "category"->"Категория", "title"->"Наименование",
    "description"->"Описание",
    "producedby"->"Производитель", "trademark"->"Торг. марка", "cars"->"Авто",
    "codeid"->"Код", "codes"->"Др. коды", "state"->"Состояние", "pic"->"Фото")

  def apply(row: Tables.GoodsRow): Entity[GoodsItem] =
    Entity(
      id = row.id,
      data = new GoodsItem(
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
        state = row.state,
        pic = row.pic
      )
    )

}


case class GoodsItem4Sale(item: Entity[GoodsItem], pic: String)

