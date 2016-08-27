package com.ruimo.scoins

import scala.collection.immutable

class EnumHelper[+T <: EnumBase] {
  private[this] var i = -1
  private[this] var allInstances: immutable.Set[EnumBase] = immutable.Set()

  def idx(e: EnumBase): Int = {
    allInstances = allInstances + e
    i = i + 1
    i
  }
  def all[U >: T]: immutable.Set[U] = allInstances.asInstanceOf[immutable.Set[U]]
}

class EnumBase(helper: EnumHelper[EnumBase]) {
  val ordinal = helper.idx(this)
}
