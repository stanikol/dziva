

case class Cl(f1:Int, f2: String)
val c = Cl(1, "11111")
c.getClass.getField("f1")