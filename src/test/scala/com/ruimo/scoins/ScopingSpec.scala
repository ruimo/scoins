package com.ruimo.scoins

import org.specs2.mutable.Specification

class ScopingSpec extends Specification {
  "Scoping" should {
    "Unit func" in {
      import Scoping.doWith
      doWith(10) {
        i => i === 10
      }
    }

    "Func returns value" in {
      import Scoping.doWith
      doWith(10) {
        i => i === 10
        i * 20
      } === 200
    }
  }
}
