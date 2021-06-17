package com.ruimo.scoins

import java.io.{InputStream, OutputStream, ByteArrayOutputStream}

import scala.annotation.tailrec

object StreamUtil {
  val CopyBufSize = 1024

  def copyStream(from: InputStream, to: OutputStream): Unit = {
    val buf = new Array[Byte](CopyBufSize)
    @tailrec def copy(): Unit = {
      val len = from.read(buf)
      if (len != -1) {
        to.write(buf, 0, len)
        copy()
      }
    }

    copy()
  }

  def readAllBytes(is: InputStream): Array[Byte] = {
    val os = new ByteArrayOutputStream()
    copyStream(is, os)
    os.toByteArray()
  }
}
