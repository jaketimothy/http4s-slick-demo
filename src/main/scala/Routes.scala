// Routes.scala
package com.jaketimothy.http4sdemo

import org.http4s.HttpService
import org.http4s.dsl._

object Routes {

  val service: HttpService = HttpService {
    // ultimately, different route groupings can be split into different files
    // TODO: define http request format for create/update

    case r @ GET -> Root / "hi" =>
      Ok(r.toString)

    // user routes: CRUD operations
    case r @ POST -> Root / "users" =>
      BadRequest("Not implemented")
      // try { 
      //   Ok(UserOperations.createUser(r))
      // } catch {
      //   case e: java.lang.NumberFormatException =>
      //     BadRequest("Command not recognized")
      // }

    case GET -> Root / "users" =>
      Ok(UserOperations.getUserList.toString)

    case GET -> Root / "users" / userId =>
      try { 
        Ok(UserOperations.getUser(userId.toInt).toString)
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    case r @ PUT -> Root / "users" / userId / "password" =>
      BadRequest("Not implemented")
      // try { 
      //   Ok(UserOperations.updateUserPassword(userId.toInt, r))
      // } catch {
      //   case e: java.lang.NumberFormatException =>
      //     BadRequest("Command not recognized")
      // }

    case DELETE -> Root / "users" / userId =>
      try { 
        Ok(UserOperations.deleteUser(userId.toInt))
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    // user routes: queries
    case GET -> Root / "users" / userId / "audit" =>
      try { 
        Ok(UserOperations.auditUser(userId.toInt))
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    // transaction routes: CRUD operations
    case r @ POST -> Root / "transactions" =>
      BadRequest("Not implemented")
      // try { 
      //   Ok(TransactionOperations.createTransaction(r))
      // } catch {
      //   case e: java.lang.NumberFormatException =>
      //     BadRequest("Command not recognized")
      // }

    case GET -> Root / "transactions" =>
      Ok(TransactionOperations.getTransactionList.toString)

    case GET -> Root / "transactions" / transactionId =>
      try { 
        Ok(TransactionOperations.getTransaction(transactionId.toInt).toString)
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    case GET -> Root / "transactions" / "user" / userId =>
      try { 
        Ok(TransactionOperations.getUserTransactions(userId.toInt).toString)
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    case GET -> Root / "transactions" / "username" / username =>
      try { 
        Ok(TransactionOperations.getUserTransactionsByUsername(username).toString)
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }

    case DELETE -> Root / "transactions" / transactionId =>
      try { 
        Ok(TransactionOperations.deleteTransaction(transactionId.toInt))
      } catch {
        case e: java.lang.NumberFormatException =>
          BadRequest("Command not recognized")
      }
  }
}
