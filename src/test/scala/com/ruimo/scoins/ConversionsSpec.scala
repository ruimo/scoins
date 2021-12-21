package com.ruimo.scoins

import Conversions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ConversionsSpec extends AnyFlatSpec with should.Matchers {
  it should "Can convert empty" in {
    toHexString(Array()) === ""
  }

  it should "Should convert 2 chars per byte" in {
    toHexString(Array(0x0)) === "00"
    toHexString(Array(0x4)) === "04"
    toHexString(Array(0xf)) === "0f"
    toHexString(Array(0x10)) === "10"
    toHexString(Array(0xff.asInstanceOf[Byte])) === "ff"
  }

  it should "Should convert multi bytes" in {
    toHexString(Array(0x0, 0x03, 0x22)) === "000322"
    toHexString(Array(0xff.asInstanceOf[Byte], 0x03, 0x22)) === "ff0322"
    toHexString(Array(3, 0xff.asInstanceOf[Byte], 0x03, 0x22)) === "03ff0322"
  }
}
