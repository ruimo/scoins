package com.ruimo.scoins

import java.io.InputStream
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
  ): Unit = {
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
    new FileInputStream(zipFile.toFile)
  ) { is =>
    explodeStream(is, toDir, maxZipEntrySize, fileNameCharset, Some(zipFile))
  } (zis => zis.close())

  def explodeStream(
    is: InputStream, toDir: Path, maxZipEntrySize: Long = 1024 * 1024 * 10,
    fileNameCharset: Charset = DefaultCharset, zipFile: Option[Path] = None
  ): immutable.Seq[Path] = {
    val zis = new ZipInputStream(is, fileNameCharset)

    @tailrec def explode(explodedFiles: immutable.Seq[Path]): immutable.Seq[Path] = Option(zis.getNextEntry()) match {
      case None =>
        explodedFiles
      case Some(ze) if ze.isDirectory =>
        zis.closeEntry()
        explode(explodedFiles)
      case Some(ze) =>
        assumeIfValidFileName(zipFile, ze.getName)
        assumeIfValidSize(zipFile, ze, maxZipEntrySize)
        val toFile = toDir.resolve(ze.getName)
        Files.createDirectories(toFile.getParent)
        Files.copy(zis, toFile)
        zis.closeEntry()
        explode(explodedFiles :+ toFile)
    }

    explode(immutable.Vector())
  }

  def assumeIfValidFileName(zipFile: Option[Path], fileName: String): Unit = {
    val f = (new File(fileName)).getCanonicalPath
    val cwd = (new File(".")).getCanonicalPath
    if (! f.startsWith(cwd))
      throw new IOException(
        "Invalid zip entry name: '" + fileName + "'" + (zipFile.map(z => " in " + z).getOrElse(""))
      )
  }

  def assumeIfValidSize(zipFile: Option[Path], zipEntry: ZipEntry, maxZipEntrySize: Long): Unit = {
    if (maxZipEntrySize < zipEntry.getSize)
      throw new IOException(
        "Too big zip entry '" + zipEntry.getName + "'" + (zipFile.map(z => " in " + z).getOrElse(""))
      )
  }

  // Check file entry name in zip file for vulnerability.
  def assumeValidFileName(zipFile: Path, fileName: String): Unit =
    assumeIfValidFileName(Some(zipFile), fileName)

  def assumeValidSize(zipFile: Path, zipEntry: ZipEntry, maxZipEntrySize: Long): Unit =
    assumeIfValidSize(Some(zipFile), zipEntry, maxZipEntrySize)
}
