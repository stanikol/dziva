import scala.collection.immutable.ListMap

/**
  * Created by stanikol on 18.09.16.
  */
package helpers {

  import models.db
  import models.db.Tables.GoodsRow

  object Hlp {

  implicit class AsMap(anyReg: AnyRef){
    def asMap: Map[String, String] = {
      object CaseClassToMap {
        def apply(cc: AnyRef) =
          (ListMap[String, String]().withDefaultValue("Eroor") /: cc.getClass.getDeclaredFields) {(a, f) =>
            f.setAccessible(true)
            a + (f.getName -> {f.get(cc) match {case Some(x) => x.toString case None => "" case x => x.toString}})
          }
      }
      CaseClassToMap.apply(anyReg)
    }
  }

implicit def convertGoodsviewRowToGoodsRow(goodsviewRow: models.db.Tables.GoodsviewRow): models.db.Tables.GoodsRow =
    GoodsRow(
      id          = goodsviewRow.id.get,
      price       = goodsviewRow.price.get,
      qnt         = goodsviewRow.qnt.get,
      category    = goodsviewRow.category.get,
      title       = goodsviewRow.title.get,
      description = goodsviewRow.description.get,
      producedby  = goodsviewRow.producedby,
      trademark   = goodsviewRow.trademark,
      cars        = goodsviewRow.cars,
      codeid      = goodsviewRow.codeid,
      codes       = goodsviewRow.codes,
      state       = goodsviewRow.state,
      pic         = goodsviewRow.pic)
  }

}
