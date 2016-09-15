package models

package object db {

  object AccountRole extends Enumeration {
    val normal, admin = Value
  }
  
  object GoodsCategories extends Enumeration {
    val Фильтра, Компрессора, Шланги, Масла, Прокладки, Всё = Value
  }
}
