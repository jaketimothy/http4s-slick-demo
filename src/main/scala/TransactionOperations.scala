// TransactionOperations.scala
package com.jaketimothy.http4sdemo

import slick.driver.H2Driver.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object TransactionOperations extends DatabaseOperations {

  val transactions = TableQuery[Transactions]

  def createTransaction(transaction: Transaction): Transaction =
    Await.result(database.run(
      (transactions returning transactions) += transaction
      ), Duration.Inf)

  def getTransactionList: List[Transaction] =
    Await.result(database.run(
      transactions.to[List].result
      ), Duration.Inf)

  def getTransactionsFromUser(userId: Int): List[Transaction] =
    Await.result(database.run(
      transactions.filter(_.fromId === userId).result
      ), Duration.Inf)
    .toList

  def getTransactionsToUser(userId: Int): List[Transaction] =
    Await.result(database.run(
      transactions.filter(_.toId === userId).result
      ), Duration.Inf)
    .toList

  def getUserTransactions(userId: Int): List[Transaction] =
    Await.result(database.run(
      transactions.filter(t => t.fromId === userId || t.toId === userId).result
      ), Duration.Inf)
    .toList

  def getUserTransactionsByUsername(username: String): List[Transaction] =
    Await.result(database.run(
      ((for {
        t <- transactions
        u <- t.from if u.username === username
        } yield t)
      union
      (for {
        t <- transactions
        u <- t.to if u.username === username
        } yield t)
      ).result
      ), Duration.Inf)
    .toList

  def getTransaction(transactionId: Int): Option[Transaction] =
    Await.result(database.run(
      transactions.filter(_.id === transactionId).result
      ), Duration.Inf)
    .headOption

  def deleteTransaction(transactionId: Int): String =
    Await.result(database.run(
      transactions.filter(_.id === transactionId).delete
      ), Duration.Inf)
    .toString
}
