package com.ruimo.scoins

import org.specs2.mutable.Specification
import java.nio.charset.StandardCharsets

class ImmutableByteArraySpec extends Specification {
  "ImmutableByteArray" should {
    "Can wrap array" in {
      val a = Array[Byte](1, 2, 3)
      val im = ImmutableByteArray.wrap(a)

      im(0) === 1
      im(1) === 2
      im(2) === 3
      im.length === 3

      a(0) = 99
      im(0) === 99
    }

    "Can create array" in {
      val a = Array[Byte](1, 2, 3)
      val im = ImmutableByteArray(a)

      im(0) === 1
      im(1) === 2
      im(2) === 3
      im.length === 3

      a(0) = 99
      im(0) === 1
    }

    "Can convert to string" in {
      val a = Array[Byte]('H', 'i')
      val im = ImmutableByteArray(a)

      im(0) === 'H'
      im(1) === 'i'
      im.length === 2
      im.asString(StandardCharsets.UTF_8) === "Hi"
    }
  }
}
