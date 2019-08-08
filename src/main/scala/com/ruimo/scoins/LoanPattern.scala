package com.ruimo.scoins

import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec
import java.io.Closeable
import java.io.Reader
import scala.language.implicitConversions


/**
  * If there is a case where your application does not open the
  * resource, use this wrapper. The wrapper keeps track with the
  * resource is used (opened) and closes the resource if it is opened
  * or just ignores the close request.  Resource wrapper is not thread
  * safe.
  */
class ResourceWrapper[T](resourceFactory: () => T)(implicit closer: T => Unit) extends AutoCloseable {
  private[scoins] var resource: Option[T] = None

  def apply(): T = resource match {
    case Some(r) => r
    case None => {
      resource = Some(resourceFactory())
      apply()
    }
  }

  def foreach(f: T => Unit) {
    resource.foreach(f)
  }

  override def close() {
    try {
      resource.foreach(r => closer(r))
    } finally {
      resource = None
    }
  }
}

@deprecated("Use scala.util.Using instead.")
object LoanPattern {
  def using[T, U](resource: T)(f: T => U)(implicit closer: T => Unit): Try[U] =
    Try(f(resource)) match {
      case Success(v) => Try {
        closer(resource)
        v
      }
      case e: Failure[U] => Try {
        closer(resource)
      } match {
        case Success(_) => e
        case fail: Failure[_] => {
          fail.exception.addSuppressed(e.exception)
          fail.asInstanceOf[Failure[U]]
        }
      }
    }

  implicit def autoCloseableCloser(resource: AutoCloseable) {
    resource.close()
  }

  implicit def closeableCloser(resource: Closeable) {
    resource.close()
  }

  @deprecated("Use using instead.")
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
