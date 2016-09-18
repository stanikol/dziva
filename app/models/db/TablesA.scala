//package models.db
//
//// AUTO-GENERATED Slick data model [2016-09-18T19:28:09.211+03:00[Europe/Kiev]]
//
///** Stand-alone Slick data model for immediate use */
//object Tables extends {
//  val profile = utils.db.TetraoPostgresDriver
//} with Tables
//
///** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
//trait Tables {
//  val profile: utils.db.TetraoPostgresDriver
//  import profile.api._
//  import slick.model.ForeignKeyAction
//  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
//  import slick.jdbc.{GetResult => GR}
//
//  /** DDL for all tables. Call .create to execute. */
//  lazy val schema: profile.SchemaDescription = Account.schema ++ Goods.schema ++ GoodsCategory.schema ++ Message.schema ++ Pics.schema
//  @deprecated("Use .schema instead of .ddl", "3.0")
//  def ddl = schema
//
//  /** Entity class storing rows of table Account
//   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
//   *  @param name Database column name SqlType(text)
//   *  @param email Database column email SqlType(text)
//   *  @param password Database column password SqlType(text)
//   *  @param role Database column role SqlType(account_role)
//   *  @param createdAt Database column created_at SqlType(timestamptz)
//   *  @param updatedAt Database column updated_at SqlType(timestamptz) */
//  case class AccountRow(id: Int, name: String, email: String, password: String, role: models.db.AccountRole.Value, createdAt: java.time.OffsetDateTime, updatedAt: java.time.OffsetDateTime)
//  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
//  implicit def GetResultAccountRow(implicit e0: GR[Int], e1: GR[String], e2: GR[models.db.AccountRole.Value], e3: GR[java.time.OffsetDateTime]): GR[AccountRow] = GR{
//    prs => import prs._
//    AccountRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[models.db.AccountRole.Value], <<[java.time.OffsetDateTime], <<[java.time.OffsetDateTime]))
//  }
//  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
//  class Account(_tableTag: Tag) extends Table[AccountRow](_tableTag, "account") {
//    def * = (id, name, email, password, role, createdAt, updatedAt) <> (AccountRow.tupled, AccountRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
//    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(email), Rep.Some(password), Rep.Some(role), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
//
//    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
//    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
//    /** Database column name SqlType(text) */
//    val name: Rep[String] = column[String]("name")
//    /** Database column email SqlType(text) */
//    val email: Rep[String] = column[String]("email")
//    /** Database column password SqlType(text) */
//    val password: Rep[String] = column[String]("password")
//    /** Database column role SqlType(account_role) */
//    val role: Rep[models.db.AccountRole.Value] = column[models.db.AccountRole.Value]("role")
//    /** Database column created_at SqlType(timestamptz) */
//    val createdAt: Rep[java.time.OffsetDateTime] = column[java.time.OffsetDateTime]("created_at")
//    /** Database column updated_at SqlType(timestamptz) */
//    val updatedAt: Rep[java.time.OffsetDateTime] = column[java.time.OffsetDateTime]("updated_at")
//
//    /** Uniqueness Index over (email) (database name account_email_key) */
//    val index1 = index("account_email_key", email, unique=true)
//  }
//  /** Collection-like TableQuery object for table Account */
//  lazy val Account = new TableQuery(tag => new Account(tag))
//
//  /** Entity class storing rows of table Goods
//   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
//   *  @param price Database column price SqlType(numeric)
//   *  @param qnt Database column qnt SqlType(int4)
//   *  @param category Database column category SqlType(varchar), Length(20,true)
//   *  @param title Database column title SqlType(varchar), Length(50,true)
//   *  @param description Database column description SqlType(varchar), Length(255,true)
//   *  @param producedby Database column producedby SqlType(varchar), Length(255,true), Default(None)
//   *  @param trademark Database column trademark SqlType(varchar), Length(50,true), Default(None)
//   *  @param cars Database column cars SqlType(varchar), Length(255,true), Default(None)
//   *  @param codeid Database column codeid SqlType(varchar), Length(50,true), Default(None)
//   *  @param codes Database column codes SqlType(varchar), Length(255,true), Default(None)
//   *  @param state Database column state SqlType(varchar), Length(20,true), Default(None)
//   *  @param pic Database column pic SqlType(int4), Default(None) */
//  case class GoodsRow(id: Int, price: scala.math.BigDecimal, qnt: Int, category: String, title: String, description: String, producedby: Option[String] = None, trademark: Option[String] = None, cars: Option[String] = None, codeid: Option[String] = None, codes: Option[String] = None, state: Option[String] = None, pic: Option[Int] = None)
//  /** GetResult implicit for fetching GoodsRow objects using plain SQL queries */
//  implicit def GetResultGoodsRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal], e2: GR[String], e3: GR[Option[String]], e4: GR[Option[Int]]): GR[GoodsRow] = GR{
//    prs => import prs._
//    GoodsRow.tupled((<<[Int], <<[scala.math.BigDecimal], <<[Int], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Int]))
//  }
//  /** Table description of table goods. Objects of this class serve as prototypes for rows in queries. */
//  class Goods(_tableTag: Tag) extends Table[GoodsRow](_tableTag, "goods") {
//    def * = (id, price, qnt, category, title, description, producedby, trademark, cars, codeid, codes, state, pic) <> (GoodsRow.tupled, GoodsRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
//    def ? = (Rep.Some(id), Rep.Some(price), Rep.Some(qnt), Rep.Some(category), Rep.Some(title), Rep.Some(description), producedby, trademark, cars, codeid, codes, state, pic).shaped.<>({r=>import r._; _1.map(_=> GoodsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12, _13)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
//
//    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
//    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
//    /** Database column price SqlType(numeric) */
//    val price: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("price")
//    /** Database column qnt SqlType(int4) */
//    val qnt: Rep[Int] = column[Int]("qnt")
//    /** Database column category SqlType(varchar), Length(20,true) */
//    val category: Rep[String] = column[String]("category", O.Length(20,varying=true))
//    /** Database column title SqlType(varchar), Length(50,true) */
//    val title: Rep[String] = column[String]("title", O.Length(50,varying=true))
//    /** Database column description SqlType(varchar), Length(255,true) */
//    val description: Rep[String] = column[String]("description", O.Length(255,varying=true))
//    /** Database column producedby SqlType(varchar), Length(255,true), Default(None) */
//    val producedby: Rep[Option[String]] = column[Option[String]]("producedby", O.Length(255,varying=true), O.Default(None))
//    /** Database column trademark SqlType(varchar), Length(50,true), Default(None) */
//    val trademark: Rep[Option[String]] = column[Option[String]]("trademark", O.Length(50,varying=true), O.Default(None))
//    /** Database column cars SqlType(varchar), Length(255,true), Default(None) */
//    val cars: Rep[Option[String]] = column[Option[String]]("cars", O.Length(255,varying=true), O.Default(None))
//    /** Database column codeid SqlType(varchar), Length(50,true), Default(None) */
//    val codeid: Rep[Option[String]] = column[Option[String]]("codeid", O.Length(50,varying=true), O.Default(None))
//    /** Database column codes SqlType(varchar), Length(255,true), Default(None) */
//    val codes: Rep[Option[String]] = column[Option[String]]("codes", O.Length(255,varying=true), O.Default(None))
//    /** Database column state SqlType(varchar), Length(20,true), Default(None) */
//    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(20,varying=true), O.Default(None))
//    /** Database column pic SqlType(int4), Default(None) */
//    val pic: Rep[Option[Int]] = column[Option[Int]]("pic", O.Default(None))
//
//    /** Foreign key referencing GoodsCategory (database name goods_category_fkey) */
//    lazy val goodsCategoryFk = foreignKey("goods_category_fkey", Rep.Some(category), GoodsCategory)(r => r.name, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
//    /** Foreign key referencing Pics (database name goods_pic_fkey) */
//    lazy val picsFk = foreignKey("goods_pic_fkey", pic, Pics)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
//  }
//  /** Collection-like TableQuery object for table Goods */
//  lazy val Goods = new TableQuery(tag => new Goods(tag))
//
//  /** Entity class storing rows of table GoodsCategory
//   *  @param id Database column id SqlType(serial), AutoInc
//   *  @param name Database column name SqlType(varchar), Length(20,true), Default(None) */
//  case class GoodsCategoryRow(id: Int, name: Option[String] = None)
//  /** GetResult implicit for fetching GoodsCategoryRow objects using plain SQL queries */
//  implicit def GetResultGoodsCategoryRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[GoodsCategoryRow] = GR{
//    prs => import prs._
//    GoodsCategoryRow.tupled((<<[Int], <<?[String]))
//  }
//  /** Table description of table goods_category. Objects of this class serve as prototypes for rows in queries. */
//  class GoodsCategory(_tableTag: Tag) extends Table[GoodsCategoryRow](_tableTag, "goods_category") {
//    def * = (id, name) <> (GoodsCategoryRow.tupled, GoodsCategoryRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
//    def ? = (Rep.Some(id), name).shaped.<>({r=>import r._; _1.map(_=> GoodsCategoryRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
//
//    /** Database column id SqlType(serial), AutoInc */
//    val id: Rep[Int] = column[Int]("id", O.AutoInc)
//    /** Database column name SqlType(varchar), Length(20,true), Default(None) */
//    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(20,varying=true), O.Default(None))
//
//    /** Uniqueness Index over (name) (database name goods_category_name_key) */
//    val index1 = index("goods_category_name_key", name, unique=true)
//  }
//  /** Collection-like TableQuery object for table GoodsCategory */
//  lazy val GoodsCategory = new TableQuery(tag => new GoodsCategory(tag))
//
//  /** Entity class storing rows of table Message
//   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
//   *  @param content Database column content SqlType(text)
//   *  @param tagList Database column tag_list SqlType(_text), Length(2147483647,false)
//   *  @param createdAt Database column created_at SqlType(timestamptz)
//   *  @param updatedAt Database column updated_at SqlType(timestamptz) */
//  case class MessageRow(id: Int, content: String, tagList: List[String], createdAt: java.time.OffsetDateTime, updatedAt: java.time.OffsetDateTime)
//  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
//  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[String], e2: GR[List[String]], e3: GR[java.time.OffsetDateTime]): GR[MessageRow] = GR{
//    prs => import prs._
//    MessageRow.tupled((<<[Int], <<[String], <<[List[String]], <<[java.time.OffsetDateTime], <<[java.time.OffsetDateTime]))
//  }
//  /** Table description of table message. Objects of this class serve as prototypes for rows in queries. */
//  class Message(_tableTag: Tag) extends Table[MessageRow](_tableTag, "message") {
//    def * = (id, content, tagList, createdAt, updatedAt) <> (MessageRow.tupled, MessageRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
//    def ? = (Rep.Some(id), Rep.Some(content), Rep.Some(tagList), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
//
//    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
//    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
//    /** Database column content SqlType(text) */
//    val content: Rep[String] = column[String]("content")
//    /** Database column tag_list SqlType(_text), Length(2147483647,false) */
//    val tagList: Rep[List[String]] = column[List[String]]("tag_list", O.Length(2147483647,varying=false))
//    /** Database column created_at SqlType(timestamptz) */
//    val createdAt: Rep[java.time.OffsetDateTime] = column[java.time.OffsetDateTime]("created_at")
//    /** Database column updated_at SqlType(timestamptz) */
//    val updatedAt: Rep[java.time.OffsetDateTime] = column[java.time.OffsetDateTime]("updated_at")
//  }
//  /** Collection-like TableQuery object for table Message */
//  lazy val Message = new TableQuery(tag => new Message(tag))
//
//  /** Entity class storing rows of table Pics
//   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
//   *  @param base64 Database column base64 SqlType(varchar) */
//  case class PicsRow(id: Int, base64: String)
//  /** GetResult implicit for fetching PicsRow objects using plain SQL queries */
//  implicit def GetResultPicsRow(implicit e0: GR[Int], e1: GR[String]): GR[PicsRow] = GR{
//    prs => import prs._
//    PicsRow.tupled((<<[Int], <<[String]))
//  }
//  /** Table description of table pics. Objects of this class serve as prototypes for rows in queries. */
//  class Pics(_tableTag: Tag) extends Table[PicsRow](_tableTag, "pics") {
//    def * = (id, base64) <> (PicsRow.tupled, PicsRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
//    def ? = (Rep.Some(id), Rep.Some(base64)).shaped.<>({r=>import r._; _1.map(_=> PicsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
//
//    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
//    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
//    /** Database column base64 SqlType(varchar) */
//    val base64: Rep[String] = column[String]("base64")
//  }
//  /** Collection-like TableQuery object for table Pics */
//  lazy val Pics = new TableQuery(tag => new Pics(tag))
//}
