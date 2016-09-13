import scala.collection.immutable.ListMap

val lb = {
  ListMap(1 -> 1, 2 -> 2)
}
for((k,v)<- lb) yield v