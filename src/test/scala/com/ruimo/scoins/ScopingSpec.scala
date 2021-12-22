package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ScopingSpec extends AnyFlatSpec with should.Matchers {
  it should "Unit func" in {
    import Scoping.doWith
    doWith(10) {
      i => assert(i === 10)
    }
  }

  it should "Func returns value" in {
    import Scoping.doWith
    doWith(10) {
      i => assert(i === 10)
        i * 20
    } should === (200)
  }
}
