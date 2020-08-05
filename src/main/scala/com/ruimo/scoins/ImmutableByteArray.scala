package com.ruimo.scoins

import java.nio.charset.Charset
import java.io.{InputStream, ByteArrayInputStream}

class ImmutableByteArray(private val table: Array[Byte]) {
  def apply(idx: Int): Byte = table(idx)
  def length: Int = table.length
  def asString(encoding: Charset): String = new String(table, encoding)
  def asString(offset: Int, length: Int, encoding: Charset): String = new String(table, offset, length, encoding)
  def inputStream: InputStream = new ByteArrayInputStream(table)
  def toByteArray: Array[Byte] = table.clone
}

object ImmutableByteArray {
  val Empty = wrap(Array[Byte]())
  def wrap(a: Array[Byte]) = new ImmutableByteArray(a)
  def apply(a: Array[Byte]) = new ImmutableByteArray(a.clone)
}
