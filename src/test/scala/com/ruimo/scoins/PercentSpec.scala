package com.ruimo.scoins

import org.specs2.mutable._

class PercentSpec extends Specification {
  "Percent" should {
    "can add" in {
      val p1 = Percent(10)
      val p2 = Percent(20)

      p1 + p2 === Percent(30)
    }

    "can subtract" in {
      val p1 = Percent(30)
      val p2 = Percent(10)
      p1 - p2 === Percent(20)
    }

    "can compare" in {
      (Percent(10) < Percent(11)) === true
      (Percent(11) > Percent(9)) === true
      (Percent(10) < Percent(9)) === false
      (Percent(11) > Percent(12)) === false
      (Percent(10) == Percent(10)) === true
      (Percent(10) == Percent(9)) === false
      (Percent(10) == Percent(11)) === false

    }
  }
}

