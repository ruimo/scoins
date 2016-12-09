package com.ruimo.scoins

import java.nio.file.{Path, Files}
import scala.collection.immutable
import com.ruimo.scoins.LoanPattern.using
import java.util.zip.{ZipInputStream, ZipEntry, ZipOutputStream}
import java.io.{File, FileInputStream, IOException, FileOutputStream}
import scala.util.Try
import scala.annotation.tailrec
import java.nio.charset.Charset

object Zip {
  val DefaultCharset = Charset.forName("Windows-31j")

  def deflate(
    zipFile: Path, files: Seq[(String, Path)], fileNameCharset: Charset = DefaultCharset
  ) {
    using(
      new ZipOutputStream(new FileOutputStream(zipFile.toFile), fileNameCharset)
    ) { zos =>
      files.foreach { case (entryName, file) =>
        zos.putNextEntry(new ZipEntry(entryName))
        Files.copy(file, zos)
        zos.closeEntry()
      }
    } (_.close())
  }

  def explode(
    zipFile: Path, toDir: Path, maxZipEntrySize: Long = 1024 * 1024 * 10,
    fileNameCharset: Charset = DefaultCharset
  ): Try[immutable.Seq[Path]] = using(
    new ZipInputStream(new FileInputStream(zipFile.toFile), fileNameCharset)
  ) { zis =>
    @tailrec def explode(explodedFiles: immutable.Seq[Path]): immutable.Seq[Path] = Option(zis.getNextEntry()) match {
      case None =>
        explodedFiles
      case Some(ze) if ze.isDirectory =>
        zis.closeEntry()
        explode(explodedFiles)
      case Some(ze) =>
        assumeValidFileName(zipFile, ze.getName)
        assumeValidSize(zipFile, ze, maxZipEntrySize)
        val toFile = toDir.resolve(ze.getName)
        Files.createDirectories(toFile.getParent)
        Files.copy(zis, toFile)
        zis.closeEntry()
        explode(explodedFiles :+ toFile)
    }

    explode(immutable.Vector())
  } (zis => zis.close())

  // Check file entry name in zip file for vulnerability.
  def assumeValidFileName(zipFile: Path, fileName: String) {
    val f = (new File(fileName)).getCanonicalPath
    val cwd = (new File(".")).getCanonicalPath
    if (! f.startsWith(cwd))
      throw new IOException("Invalid zip entry name: '" + fileName + "' in " + zipFile)
  }

  def assumeValidSize(zipFile: Path, zipEntry: ZipEntry, maxZipEntrySize: Long) {
    if (maxZipEntrySize < zipEntry.getSize)
      throw new IOException("Too big zip entry '" + zipEntry.getName + "' in " + zipFile)
  }
}
