package model

import java.io.File

object FileUtils {

  def isChildDirEmpty(f: File, child: String): Boolean = {
    assert(f.isDirectory)
    new File(f.getAbsolutePath + "/" + child).listFiles().size < 1
  }

  def getChildDirs(f: File): List[String] = {
    assert(f.isDirectory)
    f.listFiles map(f => f.getName) toList
  }
}
