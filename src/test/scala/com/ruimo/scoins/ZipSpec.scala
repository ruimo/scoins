package com.ruimo.scoins

import org.specs2.mutable._
import java.io.IOException
import java.nio.file.{Paths, Path, Files}
import java.util.zip.ZipEntry
import scala.collection.immutable

class ZipSpec extends Specification {
  "Zip" should {
    "Can assumeValidFileName detect invalid entry." in {
      Zip.assumeValidFileName(Paths.get("testdata/invalidName.zip"), "/tmp/a") must throwA[IOException]
      Zip.assumeValidFileName(Paths.get("testdata/invalidName.zip"), "tmp/a")
      1 === 1
    }

    "Can detect invalid entry." in {
      Zip.explode(Paths.get("testdata/invalidName.zip"), Paths.get(".")).get must throwA[IOException]
    }

    "Can detect invalid size entry." in {
      val ze = new ZipEntry("foo")
      ze.setSize(12345)
      Zip.assumeValidSize(Paths.get("testdata/invalidName.zip"), ze, 12345L)
      Zip.assumeValidSize(Paths.get("testdata/invalidName.zip"), ze, 12345L - 1) must throwA[IOException]
    }

    "Can explode normal zip." in {
      val dir = Files.createTempDirectory(null)
      val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/a.zip"), dir).get

      try {
        exploded.size === 1
        exploded(0) === dir.resolve("a")
        val lines = Files.readAllLines(exploded(0))
        lines.size === 1
        lines.get(0) === "Hello"
      }
      finally {
        Files.delete(exploded(0))
        Files.delete(dir)
      }
    }

    "Can explode zip with directory." in {
      val dir = Files.createTempDirectory(null)
      val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/b.zip"), dir).get
      try {
        exploded.size === 1
        exploded(0) === dir.resolve("foo/b")
        val lines = Files.readAllLines(exploded(0))
        lines.size === 1
        lines.get(0) === "World"
      }
      finally {
        Files.delete(exploded(0))
        Files.delete(dir.resolve("foo"))
        Files.delete(dir)
      }
    }
    "Can explode Japanese file." in {
      val dir = Files.createTempDirectory(null)
      val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/japanese.zip"), dir).get
      exploded.size === 1
      exploded(0) === dir.resolve("日本語.txt")
      val lines = Files.readAllLines(exploded(0))
      lines.size === 1
      lines.get(0) === "test"
    }
  }
}
