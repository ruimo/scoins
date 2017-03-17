package com.ruimo.scoins

object Formatting {
  def commaEdit(txt: String): String = {
    val signLoc = txt.indexOf("-")
    val (sign, num) =
      if (signLoc >= 0) (-1, txt.substring(0, signLoc) + txt.substring(signLoc + 1)) else (1, txt)
    f"${sign * num.toLong}%,d"
  }
}
