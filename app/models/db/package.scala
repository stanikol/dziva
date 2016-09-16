package models
package object db {

  object AccountRole extends Enumeration {
    type AccountRole = Value
    val normal, admin = Value
  }

  object GoodsCategories extends Enumeration {
    type GoodsCategories = Value
    val Фильтра, Компрессора, Шланги, Масла, Прокладки, Разное = Value
  }

}
