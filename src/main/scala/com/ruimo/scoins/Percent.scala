package com.ruimo.scoins

case class Percent(value: Double) extends AnyVal {
  def of(that: Double) = value * that / 100
  def +(that: Percent) = Percent(value + that.value)
  def -(that: Percent) = Percent(value - that.value)
}

object Percent {
  import scala.language.implicitConversions

  implicit def toPercent(d: Double) = Percent(d)
  implicit def toPercent(d: java.lang.Double) = Percent(d)
  implicit def fromPercent(p: Percent) = p.value
}

