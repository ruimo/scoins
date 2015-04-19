package com.ruimo.scoins

import scala.annotation.tailrec

object Conversions {
  def toHexString(b: Array[Byte]): String = {
    @tailrec def toHexString(b: Array[Byte], idx: Int, buf: StringBuilder): String =
      if (idx < b.size) toHexString(b, idx + 1, buf.append(byteToHexString(b(idx))))
      else buf.toString

    toHexString(b, 0, new StringBuilder(2 * b.size))
  }

  def byteToHexString(b: Byte): String = {
    val s = Integer.toHexString(0xff & b)
    if (s.size < 2) "0" + s else s
  }
}
