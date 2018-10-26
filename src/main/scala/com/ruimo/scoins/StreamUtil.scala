package com.ruimo.scoins

import java.io.{InputStream, OutputStream}

import scala.annotation.tailrec

object StreamUtil {
  val CopyBufSize = 1024

  def copyStream(from: InputStream, to: OutputStream) {
    val buf = new Array[Byte](CopyBufSize)
    @tailrec def copy() {
      val len = from.read(buf)
      if (len != -1) {
        to.write(buf, 0, len)
        copy()
      }
    }

    copy()
  }
}
