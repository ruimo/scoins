package com.ruimo.scoins

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import org.specs2.mutable.Specification

class StreamUtilSpec extends Specification {
  "StreamUtil" should {
    "copy stream of 0 byte" in {
      val from = new ByteArrayInputStream(new Array[Byte](0))
      val to = new ByteArrayOutputStream()

      StreamUtil.copyStream(from, to)
      to.toByteArray.length === 0
    }

    "copy stream of less than copy buffer size" in {
      val from = new ByteArrayInputStream(Array[Byte](0, 1, 2, 3))
      val to = new ByteArrayOutputStream()

      StreamUtil.copyStream(from, to)
      val ar = to.toByteArray
      ar.length === 4
      ar(0) === 0
      ar(1) === 1
      ar(2) === 2
      ar(3) === 3
    }

    "copy stream of just buffer size" in {
      val fromAr = new Array[Byte](StreamUtil.CopyBufSize)
      fromAr(0) = 11
      fromAr(StreamUtil.CopyBufSize - 1) = 22
      val from = new ByteArrayInputStream(fromAr)
      val to = new ByteArrayOutputStream()

      StreamUtil.copyStream(from, to)
      val toAr = to.toByteArray
      toAr.length === StreamUtil.CopyBufSize
      toAr(0) === 11
      toAr(StreamUtil.CopyBufSize - 1) === 22
    }

    "copy stream of over buffer size" in {
      val fromAr = new Array[Byte](StreamUtil.CopyBufSize + 1)
      fromAr(0) = 11
      fromAr(StreamUtil.CopyBufSize ) = 22
      val from = new ByteArrayInputStream(fromAr)
      val to = new ByteArrayOutputStream()

      StreamUtil.copyStream(from, to)
      val toAr = to.toByteArray
      toAr.length === StreamUtil.CopyBufSize + 1
      toAr(0) === 11
      toAr(StreamUtil.CopyBufSize) === 22
    }
  }
}
