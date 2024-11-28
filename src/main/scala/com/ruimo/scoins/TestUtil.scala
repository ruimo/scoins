package com.ruimo.scoins

import java.nio.file.{Files, Path, FileVisitResult, SimpleFileVisitor}
import java.nio.charset.Charset
import java.nio.file.attribute.BasicFileAttributes
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileInputStream
import com.ruimo.scoins.LoanPattern._
import scala.annotation.tailrec
import java.io.FileReader

object TestUtil {
  class BinaryFileDifferException(file0: Path, file1: Path, offset: Long)
    extends Exception(s"'${file0.toAbsolutePath}' and '${file1.toAbsolutePath} are differ (offset: ${offset})")
  class BinaryFileWithDifferentLength(file0: Path, file1: Path, len0: Long, len1: Long)
    extends Exception(s"Length is different. '${file0.toAbsolutePath}'(length: ${len0}) and '${file1.toAbsolutePath}'(length: ${len1})")

  sealed trait CompareMode
  object CompareMode {
    case object Binary extends CompareMode
    case class Text(encoding: Option[Charset] = None) extends CompareMode
  }

  def compareDir(dir0: Path, dir1: Path, mode: CompareMode = CompareMode.Text()): Unit = {
    var relativePathTbl: Set[Path] = Set()

    if (!Files.isDirectory(dir0)) throw new IllegalArgumentException(s"dir0 '${dir0.toAbsolutePath()}' is not a directory")
    if (!Files.isDirectory(dir1)) throw new IllegalArgumentException(s"dir1 '${dir1.toAbsolutePath()}' is not a directory")
    
    Files.walkFileTree(dir0, new SimpleFileVisitor[Path]() {
      override def visitFile(file0: Path, attrs: BasicFileAttributes): FileVisitResult = {
        val relativePath: Path = file0.relativize(dir0)
        val file1 = dir1.resolve(relativePath)
        
        relativePathTbl += relativePath
        compareFile(file0, file1, mode)
        
        FileVisitResult.CONTINUE
      }
    })

    Files.walkFileTree(dir1, new SimpleFileVisitor[Path]() {
      override def visitFile(file1: Path, attrs: BasicFileAttributes): FileVisitResult = {
        val relativePath: Path = file1.relativize(dir1)
        if (! relativePathTbl.contains(relativePath))
          throw new RuntimeException("'${file1.toAbsolutePath}' exists but ${dir0.resolve(relativePath).toAbsolutePath} does not exist. Relative path: '${relativePath}'")

        FileVisitResult.CONTINUE
      }
    })
  }

  def compareFile(file0: Path, file1: Path, mode: CompareMode = CompareMode.Text()): Unit = {
    if (! Files.isRegularFile(file0)) throw new IllegalArgumentException(s"file0 '${file0.toAbsolutePath()}' does not exist or not a file")
    if (! Files.isRegularFile(file1)) throw new IllegalArgumentException(s"file1 '${file1.toAbsolutePath()}' does not exist or not a file")
    
    mode match {
      case CompareMode.Binary => compareBinaryFile(file0, file1)
      case CompareMode.Text(encoding) => compareTextFile(file0, file1, encoding)
    }
  }

  def compareBinaryFile(file0: Path, file1: Path): Unit = {
    LoanPattern.using(new BufferedInputStream(new FileInputStream(file0.toFile))) { is0 =>
      LoanPattern.using(new BufferedInputStream(new FileInputStream(file1.toFile))) { is1 =>
        @tailrec def loop(offset: Long = 0): Unit = {
          val c0 = is0.read()
          val c1 = is1.read()

          if (c0 == -1 && c1 == -1) return
          if (c0 != c1)
              throw new BinaryFileDifferException(file0, file1, offset)

          loop(offset + 1)
        }
        
        val len0 = Files.size(file0)
        val len1 = Files.size(file1)
        if (len0 != len1) throw new BinaryFileWithDifferentLength(file0, file1, len0, len1)

        loop()
      }.get
    }.get
  }

  def compareTextFile(file0: Path, file1: Path, charset: Option[Charset]): Unit = {
    val fileReader0Factory: () => FileReader = () => charset match {
      case None => new FileReader(file0.toFile)
      case Some(cs) => new FileReader(file0.toFile, cs)
    }
    val fileReader1Factory: () => FileReader = () => charset match {
      case None => new FileReader(file1.toFile)
      case Some(cs) => new FileReader(file1.toFile, cs)
    }
    
    LoanPattern.using(new BufferedReader(fileReader0Factory())) { br0 =>
      LoanPattern.using(new BufferedReader(fileReader1Factory())) { br1 =>
        @tailrec def loop(line: Long = 1): Unit = {
          val l0 = br0.readLine()
          val l1 = br1.readLine()
          
          if (l0 == null) {
            if (l1 == null) return
            throw new RuntimeException(s"'${file0.toAbsolutePath}' is shorter than '${file1.toAbsolutePath}")
          } else {
            if (l1 == null) throw new RuntimeException(s"'${file1.toAbsolutePath}' is shorter than '${file0.toAbsolutePath}")
            else if (l0 != l1) {
              throw new RuntimeException(
                s"'${file0.toAbsolutePath}' and '${file1.toAbsolutePath} are differ (at line: ${line}). file0 content: '$l0', file1 content: '$l1'"
              )
              loop(line + 1)
            }
          }
        }

        loop()
      }
    }
  }
}
