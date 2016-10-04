package models

/**
  * Created by stanikol on 25.09.16.
  */
import javax.inject.Inject

import com.google.inject.Singleton
import play.api.cache.CacheApi
import play.api.inject.guice.GuiceApplicationBuilder
import services.db.DBService
import utils.db.TetraoPostgresDriver.api._

import scala.reflect.ClassTag


@Singleton
class DAO @Inject() (val database: DBService, val cache: CacheApi) {

      def categories: Seq[String] = cache.getOrElse[Seq[String]]("categories") {
        val res = database.run(models.db.Tables.GoodsCategory.map(_.name).result) //.map(_.getOrElse(""))
        cache.set("categories", res)
        res
      }

}

