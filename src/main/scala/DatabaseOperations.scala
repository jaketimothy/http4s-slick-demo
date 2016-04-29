// DatabaseManager.scala
package com.jaketimothy.http4sdemo

import org.http4s.dsl._
import slick.driver.H2Driver.api._

trait DatabaseOperations {
  import DatabaseOperations._

  def database: Database = db.get
}

object DatabaseOperations extends DatabaseOperations {
  
  private var db: Option[Database] = None

  def init(config: String): Unit =
    db = Some(Database.forConfig(config))
}
