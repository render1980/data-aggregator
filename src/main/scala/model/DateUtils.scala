package model

import com.github.nscala_time.time.Imports.{DateTime, DateTimeZone}

object DateUtils extends Serializable {
  def dtFromUtcSeconds(seconds: Int): DateTime = new DateTime(seconds * 1000L, DateTimeZone.UTC)
  def dtFromIso8601(isoString: String): DateTime = new DateTime(isoString, DateTimeZone.UTC)
}
