package com.ruimo.scoins

import java.io.Closeable
import java.io.IOException
import java.io.{Reader, StringReader}
import scala.util.{Try, Success, Failure}
import org.specs2.mutable.Specification

import org.mockito.Mockito._

class LoanPatternSpec extends Specification {
  "LoanPattern" should {
    "success" in {
      import LoanPattern.withResource

      val resource = new java.io.BufferedReader(new java.io.StringReader("Hello"))
      withResource(resource) { res =>
        res.readLine === "Hello"
        res.readLine === null
        "World"
      } === Success("World")
    }

    "access error and close ok" in {
      import LoanPattern.withResource

      val resource = mock(classOf[Closeable])
      withResource(resource) { res =>
        throw new Exception("World")
      } match {
        case Success(_) => failure
        case f: Failure[_] =>
          f.exception.getMessage === "World"
      }
      verify(resource).close()
      1 === 1
    }

    "access error and close error" in {
      import LoanPattern.withResource

      val resource = mock(classOf[Closeable])
      when(resource.close()).thenThrow(new IOException("Close"))

      withResource(resource) { res =>
        throw new Exception("World")
      } match {
        case Success(_) => failure
        case f: Failure[_] => {
          f.exception.getMessage === "Close"
          f.exception.getSuppressed().length === 1
          f.exception.getSuppressed()(0).getMessage === "World"
        }
      }
      1 === 1
    }

    "access empty reader" in {
      import LoanPattern.iteratorFromReader

      val r: Reader = new StringReader("")
      iteratorFromReader(r) { z: Iterator[Char] =>
        z.hasNext === false
      }.isSuccess === true
    }

    "access reader" in {
      import LoanPattern.iteratorFromReader

      val r: Reader = new StringReader("abc")
      iteratorFromReader(r) { z: Iterator[Char] =>
        z.hasNext === true
        z.next() === 'a'

        z.hasNext === true
        z.next() === 'b'

        z.hasNext === true
        z.next() === 'c'

        z.hasNext === false
      }.isSuccess === true
    }

    "access error in reading the first char" in {
      val resource = mock(classOf[Reader])
      when(resource.read()).thenThrow(new IOException("Error"))

      import LoanPattern.iteratorFromReader

      iteratorFromReader(resource) { z: Iterator[Char] =>
        z.hasNext
      } match {
        case Success(_) => failure
        case f: Failure[_] => {
          f.exception.getMessage === "Error"
        }
      }

      verify(resource).close()
      1 === 1
    }

    "access error in reading the second char" in {
      val resource = mock(classOf[Reader])
      when(resource.read())
        .thenReturn('a'.asInstanceOf[Int])
        .thenThrow(new IOException("Error"))

      import LoanPattern.iteratorFromReader

      iteratorFromReader(resource) { z: Iterator[Char] =>
        z.hasNext === true
        z.next === 'a'

        z.hasNext
      } match {
        case Success(_) => failure
        case f: Failure[_] => {
          f.exception.getMessage === "Error"
        }
      }

      verify(resource).close()
      1 === 1
    }

    "access error and close error in reading the second char" in {
      val resource = mock(classOf[Reader])
      when(resource.read())
        .thenReturn('a'.asInstanceOf[Int])
        .thenThrow(new IOException("Error"))
      when(resource.close())
        .thenThrow(new IOException("Close"))

      import LoanPattern.iteratorFromReader

      iteratorFromReader(resource) { z: Iterator[Char] =>
        z.hasNext === true
        z.next === 'a'

        z.hasNext
      } match {
        case Success(_) => failure
        case f: Failure[_] => {
          f.exception.getMessage === "Close"
          f.exception.getSuppressed().length === 1
          f.exception.getSuppressed()(0).getMessage === "Error"
        }
      }

      1 === 1
    }
  }
}
