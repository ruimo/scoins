package com.ruimo.scoins.encrypt

import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.Cipher

class Aes(key: SecretKeySpec, iv: IvParameterSpec) {
  def encrypt(data: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    cipher.doFinal(data)
  }

  def decrypt(encrypted: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    cipher.doFinal(encrypted)
  }
}

object Aes {
  def apply(key: Array[Byte], iv: Array[Byte]) = new Aes(
    new SecretKeySpec(key, "AES"), new IvParameterSpec(iv)
  )

  def encrypt(key: Array[Byte], iv: Array[Byte], data: Array[Byte]): Array[Byte] =
    Aes(key, iv).encrypt(data)

  def decrypt(key: Array[Byte], iv: Array[Byte], encrypted: Array[Byte]): Array[Byte] =
    Aes(key, iv).decrypt(encrypted)
}

