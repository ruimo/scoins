package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FormattingSpec extends AnyFlatSpec with should.Matchers {
  it should "perform comma edit" in {
    assert(Formatting.commaEdit("1") === "1")
    assert(Formatting.commaEdit("1-") === "-1")
    assert(Formatting.commaEdit("-1") === "-1")

    assert(Formatting.commaEdit("123") === "123")
    assert(Formatting.commaEdit("-123") === "-123")
    assert(Formatting.commaEdit("123-") === "-123")

    assert(Formatting.commaEdit("1234") === "1,234")
    assert(Formatting.commaEdit("-1234") === "-1,234")
    assert(Formatting.commaEdit("1234-") === "-1,234")

    assert(Formatting.commaEdit("123456") === "123,456")
    assert(Formatting.commaEdit("-123456") === "-123,456")
    assert(Formatting.commaEdit("123456-") === "-123,456")

    assert(Formatting.commaEdit("1234567") === "1,234,567")
    assert(Formatting.commaEdit("-1234567") === "-1,234,567")
    assert(Formatting.commaEdit("1234567-") === "-1,234,567")
  }
}

