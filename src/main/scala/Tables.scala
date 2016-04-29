// Tables.scala
package com.jaketimothy.http4sdemo

import slick.driver.H2Driver.api._
import slick.lifted.ForeignKeyQuery

case class User(
  lastname: String,
  firstname: String,
  username: String,
  password: String,
  credits: Long = 0,
  id: Option[Int] = None
  )

class Users(tag: Tag) extends Table[User](tag, "USERS") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def lastname = column[String]("LASTNAME")
  def firstname = column[String]("FIRSTNAME")
  def username = column[String]("USERNAME")
  def password = column[String]("PASSWORD")
  def credits = column[Long]("CREDITS")

  def * = (lastname, firstname, username, password, credits, id.?) <> (User.tupled, User.unapply)
}

case class Transaction(
  timestamp: Long,
  to: Int,
  from: Int,
  credits: Long,
  id: Option[Int] = None
  )

class Transactions(tag: Tag) extends Table[Transaction](tag, "TRANSACTIONS") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def timestamp = column[Long]("TIMESTAMP")
  def toId = column[Int]("TO_ID")
  def fromId = column[Int]("FROM_ID")
  def credits = column[Long]("CREDITS")

  def * = (timestamp, toId, fromId, credits, id.?) <> (Transaction.tupled, Transaction.unapply)

  def to: ForeignKeyQuery[Users, User] =
    foreignKey("TO_FK", toId, TableQuery[Users])(_.id)

  def from: ForeignKeyQuery[Users, User] =
    foreignKey("FROM_FK", fromId, TableQuery[Users])(_.id)
}
