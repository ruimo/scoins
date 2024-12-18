package com.ruimo.scoins

import scala.util.{Try, Success, Failure}
import java.nio.file.{Files, Path, FileVisitResult, SimpleFileVisitor}
import java.nio.file.attribute.FileAttribute
import java.nio.file.attribute.BasicFileAttributes
import java.io.IOException

object PathUtil {
  def deleteDir(dir: Path): Unit = {
    Files.walkFileTree(dir, new SimpleFileVisitor[Path]() {
      override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
        Files.delete(path)
        FileVisitResult.CONTINUE
      }
      override def postVisitDirectory(path: Path, error: IOException) = {
        if (error == null) {
          Files.delete(path)
          FileVisitResult.CONTINUE
        }
        else throw error
      }
    })
    Files.deleteIfExists(dir)
  }

  def withTempPath[U](
    tempFactory: () => Path, tempDestructor: Path => Unit
  )(
    func: Path => U
  ): Try[U] = {
    LoanPattern.using(tempFactory())(func)(tempDestructor)
  }

  def withTempDirectory[U](
    baseDir: Path, prefix: Option[String], attrs: FileAttribute[_]*
  )(
    func: Path => U
  )(implicit
    tempFactory: () => Path =
      () => Files.createTempDirectory(baseDir, prefix.orNull[String], attrs: _*),
    tempDestructor: Path => Unit = deleteDir
  ): Try[U] = withTempPath(tempFactory, tempDestructor)(func)

  def withTempDir[U](
    prefix: Option[String], attrs: FileAttribute[_]*
  )(
    func: Path => U
  )(implicit
    tempFactory: () => Path =
      () => Files.createTempDirectory(prefix.orNull[String], attrs: _*),
    tempDestructor: Path => Unit = deleteDir
  ): Try[U] = withTempPath(tempFactory, tempDestructor)(func)

  def withTempFile[U](
    prefix: Option[String], suffix: Option[String], attrs: FileAttribute[_]*
  )(
    func: Path => U
  )(implicit
    tempFactory: () => Path =
      () => Files.createTempFile(prefix.orNull, suffix.orNull, attrs: _*),
    tempDestructor: Path => Unit = Files.delete
    ): Try[U] = withTempPath(tempFactory, tempDestructor)(func)
    
    def copyDirs(fromDir: Path, toDir: Path): Unit = {
      Files.createDirectories(toDir)
      Files.walkFileTree(fromDir, new SimpleFileVisitor[Path]() {
        override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
          val targetDir = toDir.resolve(fromDir.relativize(dir))
          Files.createDirectories(targetDir)
          FileVisitResult.CONTINUE
        }
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          val targetFile = toDir.resolve(fromDir.relativize(file))
          Files.copy(file, targetFile)
          FileVisitResult.CONTINUE
        }
      })
    }
}
