package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets.UTF_8

class StreamUtilSpec extends AnyFlatSpec with should.Matchers {
  it should "copy stream of 0 byte" in {
    val from = new ByteArrayInputStream(new Array[Byte](0))
    val to = new ByteArrayOutputStream()

    StreamUtil.copyStream(from, to)
    assert(to.toByteArray.length === 0)
  }

  it should "copy stream of less than copy buffer size" in {
    val from = new ByteArrayInputStream(Array[Byte](0, 1, 2, 3))
    val to = new ByteArrayOutputStream()

    StreamUtil.copyStream(from, to)
    val ar = to.toByteArray
    assert(ar.length === 4)
    assert(ar(0) === 0)
    assert(ar(1) === 1)
    assert(ar(2) === 2)
    assert(ar(3) === 3)
  }

  it should "copy stream of just buffer size" in {
    val fromAr = new Array[Byte](StreamUtil.CopyBufSize)
    fromAr(0) = 11
    fromAr(StreamUtil.CopyBufSize - 1) = 22
    val from = new ByteArrayInputStream(fromAr)
    val to = new ByteArrayOutputStream()

    StreamUtil.copyStream(from, to)
    val toAr = to.toByteArray
    assert(toAr.length === StreamUtil.CopyBufSize)
    assert(toAr(0) === 11)
    assert(toAr(StreamUtil.CopyBufSize - 1) === 22)
  }

  it should "copy stream of over buffer size" in {
    val fromAr = new Array[Byte](StreamUtil.CopyBufSize + 1)
    fromAr(0) = 11
    fromAr(StreamUtil.CopyBufSize ) = 22
    val from = new ByteArrayInputStream(fromAr)
    val to = new ByteArrayOutputStream()

    StreamUtil.copyStream(from, to)
    val toAr = to.toByteArray
    assert(toAr.length === StreamUtil.CopyBufSize + 1)
    assert(toAr(0) === 11)
    assert(toAr(StreamUtil.CopyBufSize) === 22)
  }

  it should "readAllBytes can read all bytes" in {
    val in = new ByteArrayInputStream("ABCDEF".getBytes(UTF_8))
    val arr = StreamUtil.readAllBytes(in)
    assert(arr.size === 6)
    assert(arr(0) === 'A')
    assert(arr(5) === 'F')
  }
}
