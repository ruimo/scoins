package com.ruimo.scoins

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.IOException
import java.nio.file.{Files, Path, Paths}
import java.util.zip.ZipEntry
import scala.collection.immutable
import java.util.Arrays

class ZipSpec extends AnyFlatSpec with should.Matchers {
  it should "Can assumeValidFileName detect invalid entry." in {
    assertThrows[IOException] {
      Zip.assumeValidFileName(Paths.get("testdata/invalidName.zip"), "/tmp/a")
    }
    Zip.assumeValidFileName(Paths.get("testdata/invalidName.zip"), "tmp/a")
  }

  it should "Can detect invalid entry." in {
    assertThrows[IOException] {
      Zip.explode(Paths.get("testdata/invalidName.zip"), Paths.get(".")).get
    }
  }

  it should "Can detect invalid size entry." in {
    val ze = new ZipEntry("foo")
    ze.setSize(12345)
    Zip.assumeValidSize(Paths.get("testdata/invalidName.zip"), ze, 12345L)
    assertThrows[IOException] {
      Zip.assumeValidSize(Paths.get("testdata/invalidName.zip"), ze, 12345L - 1)
    }
  }

  it should "Can explode normal zip." in {
    val dir = Files.createTempDirectory(null)
    val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/a.zip"), dir).get

    try {
      assert(exploded.size === 1)
      assert(exploded(0) === dir.resolve("a"))
      val lines = Files.readAllLines(exploded(0))
      assert(lines.size === 1)
      assert(lines.get(0) === "Hello")
    }
    finally {
      Files.delete(exploded(0))
      Files.delete(dir)
    }
  }

  it should "Can explode zip with directory." in {
    val dir = Files.createTempDirectory(null)
    val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/b.zip"), dir).get
    try {
      assert(exploded.size === 1)
      assert(exploded(0) === dir.resolve("foo/b"))
      val lines = Files.readAllLines(exploded(0))
      assert(lines.size === 1)
      assert(lines.get(0) === "World")
    }
    finally {
        Files.delete(exploded(0))
      Files.delete(dir.resolve("foo"))
      Files.delete(dir)
    }
  }

  it should "Can explode Japanese file." in {
    val dir = Files.createTempDirectory(null)
    val exploded: immutable.Seq[Path] = Zip.explode(Paths.get("testdata/japanese.zip"), dir).get
    assert(exploded.size === 1)
    assert(exploded(0) === dir.resolve("日本語.txt"))
    val lines = Files.readAllLines(exploded(0))
    assert(lines.size === 1)
    assert(lines.get(0) === "test")
  }

  it should "Can deflate files." in {
    PathUtil.withTempDir(None) { dir =>
      val file1 = dir.resolve("foo.txt")
      Files.write(file1, Arrays.asList("foo"))
      val file2 = dir.resolve("bar.txt")
      Files.write(file2, Arrays.asList("bar"))
      val zipFile = dir.resolve("out.zip")

      Zip.deflate(
        zipFile,
        List("foo/foo.txt" -> file1, "bar.txt" -> file2)
      )

      PathUtil.withTempDir(None) { out =>
        val outFiles = Zip.explode(zipFile, out).get
        assert(outFiles.size === 2)
        assert(Files.readAllLines(out.resolve("foo/foo.txt")).get(0) === "foo")
        assert(Files.readAllLines(out.resolve("bar.txt")).get(0) === "bar")
      }.get
    }.get
  }
}
