package model

import java.io.File

object FileUtils {

  def getNotEmptyTopics(f: File, topic: String): String = {
    val fHist: File = new File(f.getAbsolutePath + "/" + "history")
    val histFiles = fHist.listFiles()
    println(fHist.getAbsolutePath)
    if (histFiles.size > 0)
      topic
    null
  }

  def getTopicsNames(baseDir: String): List[String] = {
    new File(baseDir)
      .listFiles()
      .map(f => getNotEmptyTopics(f, f.getName)).toList
  }
}
