package com.intellias.education.map

import com.intellias.education.TimeTracker
import com.intellias.education.map.reader.GraphOnlyReader

object RoadsLengthApp {

  def main(args: Array[String]): Unit = {
    val timeTracker = TimeTracker()
    if (args.length != 1) {
      throw new IllegalArgumentException("Exactly one argument expected: path to the OSM pbf file!")
    }
    val osmPbfFile = args(0)
    runApplication(osmPbfFile)
    timeTracker.printDurationAndReset("Finished")
  }

  private def runApplication(osmPbfFile: String): Unit = {
    val totalLength = readRoadsAndCalculateTotalLength(osmPbfFile)
    println(s"Total length: $totalLength kms")
  }

  private def readRoadsAndCalculateTotalLength(osmPbfFile: String) = {
    val waysReader = new GraphOnlyReader(osmPbfFile, 1)
    val ways = waysReader.read
    val roadNetwork = new RoadNetworkOperations(ways)
    roadNetwork.totalLength / 1000
  }
}
