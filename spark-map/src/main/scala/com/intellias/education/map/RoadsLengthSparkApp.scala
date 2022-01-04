package com.intellias.education.map

import com.intellias.education.TimeTracker

object RoadsLengthSparkApp {

  def main(args: Array[String]): Unit = {
    val timeTracker = TimeTracker()

    if (args.length < 1) {
      throw new IllegalArgumentException("At least path to the OSM pbf file is expected as an input argument!")
    }
    val osmPbfFile = args(0)
    val clusterMode = args.length == 2 && args(0) == "--clusterMode"

    runApplication(osmPbfFile, clusterMode)
    timeTracker.printDurationAndReset("Finished")
  }

  private def runApplication(osmPbfFile: String, clusterMode: Boolean): Unit = {
    val totalLength = readRoadsAndCalculateTotalLength(osmPbfFile, clusterMode)
    println(s"Total length: $totalLength kms")
  }

  private def readRoadsAndCalculateTotalLength(osmPbfFile: String, clusterMode: Boolean) = {
    ???
  }

}
