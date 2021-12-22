package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.charset.StandardCharsets

class ImmutableByteArraySpec extends AnyFlatSpec with should.Matchers {
  it should "Can wrap array" in {
    val a = Array[Byte](1, 2, 3)
    val im = ImmutableByteArray.wrap(a)

    assert(im(0) === 1)
    assert(im(1) === 2)
    assert(im(2) === 3)
    assert(im.length === 3)
    assert(im.toByteArray === a)

    a(0) = 99
    assert(im(0) === 99)
  }

  it should "Can create array" in {
    val a = Array[Byte](1, 2, 3)
    val im = ImmutableByteArray(a)

    assert(im(0) === 1)
    assert(im(1) === 2)
    assert(im(2) === 3)
    assert(im.length === 3)

    a(0) = 99
    assert(im(0) === 1)
  }

  it should "Can convert to string" in {
    val a = Array[Byte]('H', 'i')
    val im = ImmutableByteArray(a)

    assert(im(0) === 'H')
    assert(im(1) === 'i')
    assert(im.length === 2)
    assert(im.asString(StandardCharsets.UTF_8) === "Hi")
  }

  it should "Empty" in {
    assert(ImmutableByteArray.Empty.length === 0)
  }

  it should "as string with offset" in {
    assert(ImmutableByteArray(Array[Byte]('A', 'B', 'C')).asString(1, 2, StandardCharsets.ISO_8859_1) === "BC")
  }
}
