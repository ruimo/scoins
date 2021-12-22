package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PercentSpec extends AnyFlatSpec with should.Matchers {
  it should "can add" in {
    val p1 = Percent(10)
    val p2 = Percent(20)

    assert(p1 + p2 === Percent(30))
  }

  it should "can subtract" in {
    val p1 = Percent(30)
    val p2 = Percent(10)
    assert(p1 - p2 === Percent(20))
  }

  it should "can compare" in {
    assert((Percent(10) < Percent(11)) === true)
      assert((Percent(11) > Percent(9)) === true)
        assert((Percent(10) < Percent(9)) === false)
          assert((Percent(11) > Percent(12)) === false)
            assert((Percent(10) == Percent(10)) === true)
              assert((Percent(10) == Percent(9)) === false)
                assert((Percent(10) == Percent(11)) === false)
  }
}

