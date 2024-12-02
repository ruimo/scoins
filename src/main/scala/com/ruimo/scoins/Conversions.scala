package com.ruimo.scoins

import scala.annotation.tailrec
import java.io.BufferedReader

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

  def bufReaderToItr(br: BufferedReader): Iterator[String] = new Iterator[String] {
    var eof = false
    var cache: Option[String] = None

    override def hasNext: Boolean =
      if (eof) false
      else {
        if (cache.isEmpty) {
            cache = Option(br.readLine())
            if (cache.isEmpty) eof = true
        }
        cache.isDefined
      }

    override def next(): String =
      if (eof) throw new NoSuchElementException()
      else {
        if (! hasNext) throw new NoSuchElementException()
        cache match {
          case None => throw new NoSuchElementException()
          case Some(s) => {
            cache = None
            s
          }
        }
      }
  }
}
