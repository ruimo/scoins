package com.ruimo.scoins.encrypt

import org.specs2.mutable.Specification
import java.security.GeneralSecurityException

class AesSpec extends Specification {
  "Aes" should {
    "Can encrypt text using convenience method" in {
      val text = "Hello, World"
      val iv = "1234567890123456"
      val key = "My AESEncryptKey"

      val encrypted = Aes.encrypt(key.getBytes("UTF-8"), iv.getBytes("UTF-8"), text.getBytes("UTF-8"))
      encrypted !== text.getBytes("UTF-8")

      Aes.decrypt(key.getBytes("UTF-8"), iv.getBytes("UTF-8"), encrypted) === text.getBytes("UTF-8")
      Aes.decrypt(key.getBytes("UTF-8"), "6543210987654321".getBytes("UTF-8"), encrypted) !== text.getBytes("UTF-8")
      Aes.decrypt("No AESEncryptKey".getBytes("UTF-8"), iv.getBytes("UTF-8"), encrypted) must throwAn[GeneralSecurityException]
    }

    "Can encrypt text using Aes class" in {
      val text = "Hello, World"
      val iv = "1234567890123456"
      val key = "My AESEncryptKey"
      val aes = Aes(key.getBytes("UTF-8"), iv.getBytes("UTF-8"))

      val encrypted = aes.encrypt(text.getBytes("UTF-8"))
      encrypted !== text.getBytes("UTF-8")
      aes.decrypt(encrypted) === text.getBytes("UTF-8")
    }
  }
}

