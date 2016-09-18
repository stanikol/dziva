import scala.collection.immutable.ListMap

/**
  * Created by stanikol on 18.09.16.
  */
package helpers {

  import models.db

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

}

}
