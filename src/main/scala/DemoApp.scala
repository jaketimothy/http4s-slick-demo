// DemoApp.scala
package com.jaketimothy.http4sdemo

import org.http4s.server.blaze.BlazeBuilder
import slick.driver.H2Driver.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object DemoApp {
  
  def main(args: Array[String]): Unit = {

    DatabaseOperations.init("h2demo")

    val db = DatabaseOperations.database

    try {
      // populate tables with data
      Await.result(db.run(DBIO.seq(
        UserOperations.users.schema.create,
        UserOperations.users += User("Bank", "Master", "bank", "password", 1000),
        UserOperations.users += User("Timothy", "Jake", "jaketimothy", "password"),
        UserOperations.users += User("Last", "First", "firstlast", "password", 100)
        )), Duration.Inf)

      val bank = UserOperations.getUserByUsername("bank").get
      val jake = UserOperations.getUserByUsername("jaketimothy").get
      val person = UserOperations.getUserByUsername("firstlast").get

      Await.result(db.run(DBIO.seq(
        TransactionOperations.transactions.schema.create,
        TransactionOperations.transactions += Transaction(1, jake.id.get, bank.id.get, 100),
        TransactionOperations.transactions += Transaction(1, bank.id.get, jake.id.get, -50),
        TransactionOperations.transactions += Transaction(1, person.id.get, bank.id.get, 100),
        TransactionOperations.transactions += Transaction(1, bank.id.get, bank.id.get, 500)
        )), Duration.Inf)

      // launch http service
      BlazeBuilder
        .bindHttp(8080)
        .mountService(Routes.service, "/api")
        .run
        .awaitShutdown()
    } finally db.close
  }
}
