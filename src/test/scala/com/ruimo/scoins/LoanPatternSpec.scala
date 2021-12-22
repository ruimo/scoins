package com.ruimo.scoins

import java.io.Closeable
import java.io.IOException
import java.io.{Reader, StringReader}
import scala.util.{Failure, Success, Try}
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class LoanPatternSpec extends AnyFlatSpec with should.Matchers {
  it should "success" in {
    import LoanPattern.withResource

    val resource = new java.io.BufferedReader(new java.io.StringReader("Hello"))
    withResource(resource) { res =>
      assert(res.readLine === "Hello")
      assert(res.readLine === null)
      "World"
    } should === (Success("World"))
  }

  it should "success for using" in {
    import LoanPattern.using
      import LoanPattern.autoCloseableCloser

    val resource = new java.io.BufferedReader(new java.io.StringReader("Hello"))
    using(resource) { res =>
      assert(res.readLine === "Hello")
      assert(res.readLine === null)
      "World"
    } should === (Success("World"))
  }

  it should "access error and close ok" in {
    import LoanPattern.withResource

    val resource = mock(classOf[Closeable])
    withResource(resource) { res =>
      throw new Exception("World")
    } match {
      case Success(_) => fail
      case f: Failure[_] =>
        assert(f.exception.getMessage === "World")
    }
    verify(resource).close()
  }

  it should "access error and close ok for using" in {
    import LoanPattern.using
      import LoanPattern.closeableCloser

    val resource = mock(classOf[Closeable])
    using(resource) { res =>
      throw new Exception("World")
    } match {
      case Success(_) => fail
      case f: Failure[_] =>
        assert(f.exception.getMessage === "World")
    }
    verify(resource).close()
  }

  it should "access error and close error" in {
    import LoanPattern.withResource

    val resource = mock(classOf[Closeable])
    when(resource.close()).thenThrow(new IOException("Close"))

    withResource(resource) { res =>
      throw new Exception("World")
    } match {
      case Success(_) => fail
      case f: Failure[_] => {
        assert(f.exception.getMessage === "Close")
        assert(f.exception.getSuppressed().length === 1)
        assert(f.exception.getSuppressed()(0).getMessage === "World")
      }
    }
  }

  it should "access error and close error for using" in {
    import LoanPattern.using
      import LoanPattern.closeableCloser

    val resource = mock(classOf[Closeable])
    when(resource.close()).thenThrow(new IOException("Close"))

    using(resource) { res =>
      throw new Exception("World")
    } match {
      case Success(_) => fail
      case f: Failure[_] => {
        assert(f.exception.getMessage === "Close")
        assert(f.exception.getSuppressed().length === 1)
        assert(f.exception.getSuppressed()(0).getMessage === "World")
      }
    }
  }

  it should "access empty reader" in {
    import LoanPattern.iteratorFromReader

    val r: Reader = new StringReader("")
    iteratorFromReader(r) { (z: Iterator[Char]) =>
      assert(z.hasNext === false)
    }.isSuccess should === (true)
  }

  it should "access reader" in {
    import LoanPattern.iteratorFromReader

    val r: Reader = new StringReader("abc")
    iteratorFromReader(r) { (z: Iterator[Char]) =>
      assert(z.hasNext === true)
      assert(z.next() === 'a')

      assert(z.hasNext === true)
      assert(z.next() === 'b')

      assert(z.hasNext === true)
      assert(z.next() === 'c')

      assert(z.hasNext === false)
    }.isSuccess should === (true)
  }

  it should "access error in reading the first char" in {
    val resource = mock(classOf[Reader])
    when(resource.read()).thenThrow(new IOException("Error"))

    import LoanPattern.iteratorFromReader

    iteratorFromReader(resource) { (z: Iterator[Char]) =>
      z.hasNext
    } match {
      case Success(_) => fail
      case f: Failure[_] => {
        assert(f.exception.getMessage === "Error")
      }
    }

    verify(resource).close()
  }

  it should "access error in reading the second char" in {
    val resource = mock(classOf[Reader])
    when(resource.read())
      .thenReturn('a'.asInstanceOf[Int])
      .thenThrow(new IOException("Error"))

    import LoanPattern.iteratorFromReader

    iteratorFromReader(resource) { (z: Iterator[Char]) =>
      assert(z.hasNext === true)
      assert(z.next === 'a')

      z.hasNext
    } match {
      case Success(_) => fail
      case f: Failure[_] => {
        assert(f.exception.getMessage === "Error")
      }
    }

    verify(resource).close()
  }

  it should "access error and close error in reading the second char" in {
    val resource = mock(classOf[Reader])
    when(resource.read())
      .thenReturn('a'.asInstanceOf[Int])
      .thenThrow(new IOException("Error"))
    when(resource.close())
      .thenThrow(new IOException("Close"))

    import LoanPattern.iteratorFromReader

    iteratorFromReader(resource) { (z: Iterator[Char]) =>
      assert(z.hasNext === true)
      assert(z.next === 'a')

      z.hasNext
    } match {
      case Success(_) => fail
      case f: Failure[_] => {
        assert(f.exception.getMessage === "Close")
        assert(f.exception.getSuppressed().length === 1)
        assert(f.exception.getSuppressed()(0).getMessage === "Error")
      }
    }
  }

  it should "resource wrapper closes resource" in {
    val resource = mock(classOf[Reader])

    import LoanPattern._
    using(new ResourceWrapper(() => resource)) { r =>
      assert(r() === resource)
    }

    verify(resource).close()
  }

  it should "resource wrapper does not close resource when the resource is not used" in {
    val resource = mock(classOf[Reader])

    import LoanPattern._
    using(new ResourceWrapper(() => resource)) { r =>
    }

    verify(resource, never()).close()
  }
}
