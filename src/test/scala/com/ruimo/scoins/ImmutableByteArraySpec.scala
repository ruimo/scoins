package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.charset.StandardCharsets

class ImmutableByteArraySpec extends AnyFlatSpec with should.Matchers {
  it should "Can wrap array" in {
    val a = Array[Byte](1, 2, 3)
    val im = ImmutableByteArray.wrap(a)

    im(0) === 1
    im(1) === 2
    im(2) === 3
    im.length === 3
    im.toByteArray === a

    a(0) = 99
    im(0) === 99
  }

  it should "Can create array" in {
    val a = Array[Byte](1, 2, 3)
    val im = ImmutableByteArray(a)

    im(0) === 1
    im(1) === 2
    im(2) === 3
    im.length === 3

    a(0) = 99
    im(0) === 1
  }

  it should "Can convert to string" in {
    val a = Array[Byte]('H', 'i')
    val im = ImmutableByteArray(a)

    im(0) === 'H'
    im(1) === 'i'
    im.length === 2
    im.asString(StandardCharsets.UTF_8) === "Hi"
  }

  it should "Empty" in {
    ImmutableByteArray.Empty.length === 0
  }

  it should "as string with offset" in {
    ImmutableByteArray(Array[Byte]('A', 'B', 'C')).asString(1, 2, StandardCharsets.ISO_8859_1) === "BC"
  }
}
