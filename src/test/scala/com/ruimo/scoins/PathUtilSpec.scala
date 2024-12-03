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
        assert(Files.list(to).toArray().length === 0)
      }
    }
    1 === 1
  }

  "Can copy a directory" in {
    PathUtil.withTempDir(None) { dir0 => 
      val from = dir0.resolve("from")
      Files.createDirectories(from)
      Files.write(from.resolve("a"), "a".getBytes())
      val b = from.resolve("b")
      Files.createDirectories(b)
      Files.write(b.resolve("c"), "c".getBytes())

      PathUtil.withTempDir(None) { dir1 =>
        val to = dir1.resolve("to")
        
        PathUtil.copyDirs(from, to)
        assert(Files.isDirectory(to) === true)
        assert(Files.list(to).toArray().length === 2)
        assert(Files.readAllBytes(to.resolve("a")) === "a".getBytes())
        assert(Files.readAllBytes(to.resolve("b/c")) === "c".getBytes())
      }
    }
    1 === 1
  }
}