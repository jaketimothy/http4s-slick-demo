// UserOperations.scala
package com.jaketimothy.http4sdemo

import slick.driver.H2Driver.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object UserOperations extends DatabaseOperations {

  val users = TableQuery[Users]

  def createUser(user: User): User =
    Await.result(database.run(
      (users returning users) += user
      ), Duration.Inf)

  def getUserList: List[User] =
    Await.result(database.run(
      users.to[List].result
      ), Duration.Inf)

  def getUser(userId: Int): Option[User] =
    Await.result(database.run(
      users.filter(_.id === userId).result
      ), Duration.Inf)
    .headOption

  def getUserByUsername(username: String): Option[User] =
    Await.result(database.run(
      users.filter(_.username === username).result
      ), Duration.Inf)
    .headOption

  def updateUserPassword(userId: Int, password: String): Option[User] = {
    Await.result(database.run(
      users.filter(_.id === userId).map(x => (x.password)).update((password))
      ), Duration.Inf)
    getUser(userId)
  }

  def deleteUser(userId: Int): String =
    Await.result(database.run(
      users.filter(_.id === userId).delete
      ), Duration.Inf)
    .toString

  def auditUser(userId: Int): String = {

    val userCredits: Long = Await.result(database.run(
      users.filter(_.id === userId).map(_.credits).result
      ), Duration.Inf)
      .headOption
      .getOrElse(0)

    val creditsTo: Long = Await.result(database.run(
      TransactionOperations.transactions.filter(_.toId === userId).map(_.credits).sum.result
      ), Duration.Inf)
      .getOrElse(0)
    val creditsFrom: Long = Await.result(database.run(
      TransactionOperations.transactions.filter(_.fromId === userId).map(_.credits).sum.result
      ), Duration.Inf)
      .getOrElse(0)

    if (userCredits == creditsTo - creditsFrom) {
      "Audit passed"
    } else {
      s"Audit failed\n user: $userCredits, from transactions: ${creditsTo - creditsFrom}"
    }
  }
}
