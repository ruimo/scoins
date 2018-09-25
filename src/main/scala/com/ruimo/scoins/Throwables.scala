package com.ruimo.scoins

import com.ruimo.scoins.LoanPattern._
import java.io.{PrintWriter, StringWriter}

object Throwables {
  def stackTrace(t: Throwable): String = using(new StringWriter()) { sw =>
    using(new PrintWriter(sw)) { pw =>
      t.printStackTrace(pw)
    }.get
    sw.toString
  }.get
}
