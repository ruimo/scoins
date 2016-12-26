package com.ruimo.scoins

import java.nio.charset.Charset
import java.io.{InputStream, ByteArrayInputStream}

class ImmutableByteArray(private val table: Array[Byte]) {
  def apply(idx: Int): Byte = table(idx)
  def length: Int = table.length
  def asString(encoding: Charset): String = new String(table, encoding)
  def inputStream: InputStream = new ByteArrayInputStream(table)
}

object ImmutableByteArray {
  def wrap(a: Array[Byte]) = new ImmutableByteArray(a)
  def apply(a: Array[Byte]) = new ImmutableByteArray(a.clone)
}
