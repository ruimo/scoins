package com.ruimo.scoins

import java.nio.file.{Files, Path}
import javax.xml.bind.DatatypeConverter

object Base64 {
  def encode(path: Path): String = encode(Files.readAllBytes(path))
  def encode(data: Array[Byte]): String = DatatypeConverter.printBase64Binary(data)
}
