package com.ruimo.scoins.encrypt

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.security.GeneralSecurityException

class AesSpec extends AnyFlatSpec with should.Matchers {
  it should "Can encrypt text using convenience method" in {
    val text = "Hello, World"
    val iv = "1234567890123456"
    val key = "My AESEncryptKey"

    val encrypted = Aes.encrypt(key.getBytes("UTF-8"), iv.getBytes("UTF-8"), text.getBytes("UTF-8"))
    encrypted !== text.getBytes("UTF-8")

    assert(Aes.decrypt(key.getBytes("UTF-8"), iv.getBytes("UTF-8"), encrypted) === text.getBytes("UTF-8"))
    Aes.decrypt(key.getBytes("UTF-8"), "6543210987654321".getBytes("UTF-8"), encrypted) !== text.getBytes("UTF-8")
    assertThrows[GeneralSecurityException] {
      Aes.decrypt("No AESEncryptKey".getBytes("UTF-8"), iv.getBytes("UTF-8"), encrypted)
    }
  }

  it should "Can encrypt text using Aes class" in {
    val text = "Hello, World"
    val iv = "1234567890123456"
    val key = "My AESEncryptKey"
    val aes = Aes(key.getBytes("UTF-8"), iv.getBytes("UTF-8"))

    val encrypted = aes.encrypt(text.getBytes("UTF-8"))
    encrypted !== text.getBytes("UTF-8")
    assert(aes.decrypt(encrypted) === text.getBytes("UTF-8"))
  }

  it should "toLength works" in {
    assert(Aes.toLength(Array[Byte](0, 1, 2), 3) === Array[Byte](0, 1, 2))
    assert(Aes.toLength(Array[Byte](0, 1, 2), 2) === Array[Byte](0, 1))
    assert(Aes.toLength(Array[Byte](0, 1, 2), 7) === Array[Byte](0, 1, 2, 0, 1, 2, 0))
  }
}

