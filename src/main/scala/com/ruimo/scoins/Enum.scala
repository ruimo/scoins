package com.ruimo.scoins

import scala.collection.immutable

class EnumHelper[+T <: EnumBase] {
  private[this] var i = -1
  private[this] var byOrdinal: immutable.IntMap[T] = immutable.IntMap()

  def idx(e: EnumBase): Int = {
    i = i + 1
    byOrdinal = byOrdinal.updated(i, e.asInstanceOf[T])
    i
  }
  def all[U >: T]: immutable.IntMap[U] = byOrdinal.asInstanceOf[immutable.IntMap[U]]
}

class EnumBase(helper: EnumHelper[EnumBase]) {
  val ordinal = helper.idx(this)
}
