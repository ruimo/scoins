package com.ruimo.scoins

import org.specs2.mutable.Specification
import java.nio.file.Paths
import java.nio.file.Files

class PathUtilSpec extends Specification {
  "Can copy empty directory" in {
    PathUtil.withTempDir(None) { dir0 => 
      val from = dir0.resolve("from")
      Files.createDirectories(from)
      
      PathUtil.withTempDir(None) { dir1 =>
        val to = dir1.resolve("to")
        
        PathUtil.copyDirs(from, to)
        assert(Files.isDirectory(to) === true)
        assert(Files.list(to).toList().size() === 0)
      }
    }
    1 === 1
  }

  "Can copy a directory" in {
    PathUtil.withTempDir(None) { dir0 => 
      val from = dir0.resolve("from")
      Files.createDirectories(from)
      Files.writeString(from.resolve("a"), "a")
      val b = from.resolve("b")
      Files.createDirectories(b)
      Files.writeString(b.resolve("c"), "c")

      PathUtil.withTempDir(None) { dir1 =>
        val to = dir1.resolve("to")
        
        PathUtil.copyDirs(from, to)
        assert(Files.isDirectory(to) === true)
        assert(Files.list(to).toList().size() === 2)
        assert(Files.readString(to.resolve("a")) === "a")
        assert(Files.readString(to.resolve("b/c")) === "c")
      }
    }
    1 === 1
  }
}