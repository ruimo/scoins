package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PercentSpec extends AnyFlatSpec with should.Matchers {
  it should "can add" in {
    val p1 = Percent(10)
    val p2 = Percent(20)

    p1 + p2 === Percent(30)
  }

  it should "can subtract" in {
    val p1 = Percent(30)
    val p2 = Percent(10)
    p1 - p2 === Percent(20)
  }

  it should "can compare" in {
    (Percent(10) < Percent(11)) === true
    (Percent(11) > Percent(9)) === true
    (Percent(10) < Percent(9)) === false
    (Percent(11) > Percent(12)) === false
    (Percent(10) == Percent(10)) === true
    (Percent(10) == Percent(9)) === false
    (Percent(10) == Percent(11)) === false
  }
}

