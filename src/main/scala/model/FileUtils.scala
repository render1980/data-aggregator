package model

import java.io.File

object FileUtils {

  def isEmpty(f: File, topic: String): Boolean = {
    new File(f.getAbsolutePath + "/" + "history").listFiles().size < 1
  }

  def getTopicsNames(baseDir: String): List[String] = {
    new File(baseDir)
      .listFiles()
      .filter(f => !isEmpty(f, f.getName)).map(f => f.getName).toList
  }
}
