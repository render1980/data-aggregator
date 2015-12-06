package model

import au.com.bytecode.opencsv.CSVReader
import scala.collection.JavaConversions._
import java.io.{FileReader, File}

object FileUtils {

  def isChildDirEmpty(f: File, child: String): Boolean = {
    assert(f.isDirectory)
    new File(f.getAbsolutePath + "/" + child).listFiles().size < 1
  }

  def getChildDirs(f: File): List[String] = {
    assert(f.isDirectory)
    f.listFiles map(f => f.getName) toList
  }

  def readCsvFile(f: File, header: Boolean): List[Map[String, String]] = {
    val reader: CSVReader = new CSVReader(new FileReader(f))
    if (header)
      reader.readNext()
    val rows = for (row <- reader.readAll()) yield {
      Map(row(0) -> row(1))
    }
    val resList = rows.toList
    reader.close
    resList
  }
}
