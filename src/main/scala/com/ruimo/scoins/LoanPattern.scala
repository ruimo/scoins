package com.ruimo.scoins

import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec
import java.io.Closeable
import java.io.Reader

object LoanPattern {
  def withResource[T <% Closeable, U](resource: T)(f: T => U): Try[U] =
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

  def iteratorFromReader[T <% Reader, U](resource: T)(f: Iterator[Char] => U): Try[U] =
    withResource(resource) { reader =>
      f(new Iterator[Char] {
        var ch: Int = -2 // -2 means buffer empty

        def hasNext: Boolean = {
          if (ch == -1) false
          else if (ch == -2) {
            ch = reader.read()
          }

          0 <= ch
        }

        def next(): Char = if (hasNext) {
          try {
            ch.asInstanceOf[Char]
          }
          finally {
            ch = -2
          }
        }
        else throw new NoSuchElementException
      })
    }
}
