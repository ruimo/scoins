package com.ruimo.scoins

import Conversions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import java.io.StringReader
import java.io.BufferedReader

class ConversionsSpec extends AnyFlatSpec with should.Matchers {
  it should "Can convert empty" in {
    assert(toHexString(Array()) === "")
  }

  it should "Should convert 2 chars per byte" in {
    assert(toHexString(Array(0x0)) === "00")
    assert(toHexString(Array(0x4)) === "04")
    assert(toHexString(Array(0xf)) === "0f")
    assert(toHexString(Array(0x10)) === "10")
    assert(toHexString(Array(0xff.asInstanceOf[Byte])) === "ff")
  }

  it should "Should convert multi bytes" in {
    assert(toHexString(Array(0x0, 0x03, 0x22)) === "000322")
    assert(toHexString(Array(0xff.asInstanceOf[Byte], 0x03, 0x22)) === "ff0322")
    assert(toHexString(Array(3, 0xff.asInstanceOf[Byte], 0x03, 0x22)) === "03ff0322")
  }

  it should "Convert empty buffered reader" in {
    val br = new BufferedReader(new StringReader(""))
    val itr = bufReaderToItr(br)
    assert(itr.hasNext === false)

    assertThrows[NoSuchElementException] {
      itr.next()
    }
  }

  it should "Convert a buffered reader" in {
    val br = new BufferedReader(new StringReader("ABC\nDEF"))
    val itr = bufReaderToItr(br)
    assert(itr.hasNext === true)
    assert(itr.next() === "ABC")
    assert(itr.hasNext === true)
    assert(itr.hasNext === true)
    assert(itr.next() === "DEF")

    assert(itr.hasNext === false)
    assert(itr.hasNext === false)
    assertThrows[NoSuchElementException] {
      itr.next()
    }
  }
}
