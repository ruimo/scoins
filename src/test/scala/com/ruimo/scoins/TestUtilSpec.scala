package com.ruimo.scoins

import org.specs2.mutable.Specification
import java.nio.file.Paths
import java.nio.file.Files

class TestUtilSpec extends Specification {
  "Dir0 does not exist" in {
    TestUtil.compareDir(Paths.get("non-existent"), Paths.get("non-existent")) must throwA[IllegalArgumentException]
  }

  "Dir1 does not exist" in {
    PathUtil.withTempDir(None) { dir0 =>
      TestUtil.compareDir(dir0, Paths.get("non-existent")) must throwA[IllegalArgumentException]
    }.get
  }

  "Dir0 is not a directory" in {
    PathUtil.withTempFile(None, None) { file0 =>
      TestUtil.compareDir(file0, Paths.get("non-existent")) must throwA[IllegalArgumentException]
    }.get
  }

  "Dir1 is not a directory" in {
    PathUtil.withTempDir(None) { dir0 =>
      PathUtil.withTempFile(None, None) { file1 =>
        TestUtil.compareDir(dir0, file1) must throwA[IllegalArgumentException]
      }.get
    }.get
  }

  "File 0 does not exist" in {
    TestUtil.compareFile(Paths.get("non-existent"), Paths.get("non-existent")) must throwA[IllegalArgumentException]
  }

  "File1 does not exist" in {
    PathUtil.withTempFile(None, None) { file0 =>
      TestUtil.compareFile(file0, Paths.get("non-existent")) must throwA[IllegalArgumentException]
    }.get
  }

  "File 0 is not a file" in {
    PathUtil.withTempDir(None) { dir0 =>
      TestUtil.compareFile(dir0, Paths.get("non-existent")) must throwA[IllegalArgumentException]
    }.get
  }

  "File 1 is not a file" in {
    PathUtil.withTempFile(None, None) { file0 =>
      PathUtil.withTempDir(None) { dir1 =>
        TestUtil.compareFile(file0, dir1) must throwA[IllegalArgumentException]
      }.get
    }.get
  }

  "Files are identical in binary mode" in {
    PathUtil.withTempFile(None, None) { file0 =>
      PathUtil.withTempFile(None, None) { file1 =>
        Files.write(file0, Array[Byte](0, 1, 2))
        Files.write(file1, Array[Byte](0, 1, 2))

        TestUtil.compareFile(file0, file1, TestUtil.CompareMode.Binary)
      }.get
    }.get
    1 === 1
  }

  "Files are different in binary mode" in {
    PathUtil.withTempFile(None, None) { file0 =>
      PathUtil.withTempFile(None, None) { file1 =>
        Files.write(file0, Array[Byte](0, 1, 2))
        Files.write(file1, Array[Byte](0, 1, 3))

        TestUtil.compareFile(file0, file1, TestUtil.CompareMode.Binary) must throwA[RuntimeException]
      }.get
    }.get
    1 === 1
  }

  "Files are identical in text mode" in {
    PathUtil.withTempFile(None, None) { file0 =>
      PathUtil.withTempFile(None, None) { file1 =>
        Files.write(file0, "A\r\nB".getBytes())
        Files.write(file1, "A\nB".getBytes())

        TestUtil.compareFile(file0, file1, TestUtil.CompareMode.Text())
      }.get
    }.get
    1 === 1
  }

  "Files are different in text mode" in {
    PathUtil.withTempFile(None, None) { file0 =>
      PathUtil.withTempFile(None, None) { file1 =>
        Files.write(file0, "A\r\nBC".getBytes())
        Files.write(file1, "A\nB".getBytes())

        TestUtil.compareFile(file0, file1, TestUtil.CompareMode.Text()) must throwA[RuntimeException]
      }.get
    }.get
    1 === 1
  }
}
