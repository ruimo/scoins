package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class VersionSpec extends AnyFlatSpec with should.Matchers {
  it should "Version no hold value." in {
    VersionNo(123).value === 123
  }

  it should "Version can detect error." in {
    assertThrows[IllegalArgumentException] {
      VersionNo(VersionNo.MaxValue + 1)
    }
  }

  it should "Can parse major version" in {
    Version.parse("1") === VersionMajor(VersionNo(1), false)
    Version.parse("1-SNAPSHOT") === VersionMajor(VersionNo(1), true)
  }

  it should "Can parse major/minor version" in {
    Version.parse("1.2") === VersionMajorMinor(VersionNo(1), VersionNo(2), false)
    Version.parse("1.2-SNAPSHOT") === VersionMajorMinor(VersionNo(1), VersionNo(2), true)
  }

  it should "Can parse major/minor/patch version" in {
    Version.parse("1.2.3") === VersionMajorMinorPatch(VersionNo(1), VersionNo(2), VersionNo(3), false)
    Version.parse("1.2.3-SNAPSHOT") === VersionMajorMinorPatch(VersionNo(1), VersionNo(2), VersionNo(3), true)
  }
}
