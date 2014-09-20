package com.ruimo.scoins

object Scoping {
  def doWith[T, U](arg: T)(func: T => U): U = func(arg)
}
