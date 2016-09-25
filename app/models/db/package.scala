package models

import javax.inject.{Inject, Singleton}

import com.google.inject.{AbstractModule, Binder, Guice, Module}
import play.api.{Play, cache}
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import services.db.{DBService, DBServiceImpl}

import scala.concurrent.Future
import scala.reflect.ClassTag
import play.api.cache._

package object db {

  object AccountRole extends Enumeration {
    type AccountRole = Value
    val normal, admin = Value
  }


//  import utils.database.TetraoPostgresDriver.api._
//
//
//  object GuiceUtils {
//    lazy val injector = new GuiceApplicationBuilder().injector()
//    def inject[T: ClassTag]: T = injector.instanceOf[T]
//  }
//
//
//  object Categories {
//    val database: DBService = GuiceUtils.inject[DBService]
//    val cache: CacheApi = play.api.Play.current.injector.instanceOf(classOf[CacheApi])
////    val cach: CacheApi = GuiceUtils.inject[CacheApi]
//    val values = cache.getOrElse[Seq[String]]("categories"){
//      val res = database.run(models.database.Tables.GoodsCategory.map(_.name).result).map(_.getOrElse(""))
//      cache.set("categories", res)
//      res
//    }
////    val database = play.api.Play.current.injector.instanceOf(classOf[DBService])
////    val values = database.run(models.database.Tables.GoodsCategory.map(_.name).result).map(_.getOrElse(""))
//
//  }


}
