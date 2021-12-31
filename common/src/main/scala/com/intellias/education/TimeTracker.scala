package com.intellias.education

import java.time.{Duration, LocalDateTime}

class TimeTracker {

  var start: LocalDateTime = now

  def printDurationAndReset(name: String): TimeTracker = {
    println(s"$name: (${Duration.between(now, start)})")
    start = now
    this
  }

  def now: LocalDateTime = LocalDateTime.now()

}

object TimeTracker {
  def apply(): TimeTracker = new TimeTracker()
}
