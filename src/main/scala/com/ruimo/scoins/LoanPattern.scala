package com.ruimo.scoins

import scala.util.{Try, Success, Failure}
import java.io.Closeable

object LoanPattern {
  def withResource[T <: Closeable, U](resource: T)(f: T => U): Try[U] =
    Try(f(resource)) match {
      case Success(v) => Try {
        resource.close()
        v
      }

      case e: Failure[U] => Try {
        resource.close()
      } match {
        case Success(_) => e
        case fail: Failure[_] => {
          fail.exception.addSuppressed(e.exception)
          fail.asInstanceOf[Failure[U]]
        }
      }
    }
}
