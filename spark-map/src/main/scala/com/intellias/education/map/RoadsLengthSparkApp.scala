package com.intellias.education.map

import com.intellias.education.TimeTracker
import com.intellias.education.map.geometry.dist.LengthCalculator
import com.intellias.education.map.model.Point
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}

object RoadsLengthSparkApp {

  def main(args: Array[String]): Unit = {
    val timeTracker = TimeTracker()

    if (args.length < 1) {
      throw new IllegalArgumentException("At least path to the OSM pbf file is expected as an input argument!")
    }
    val osmOrcFile = args(0)
    val clusterMode = args.length == 2 && args(0) == "--clusterMode"

    runApplication(osmOrcFile, clusterMode)
    timeTracker.printDurationAndReset("Finished")
  }

  private def runApplication(osmOrcFile: String, clusterMode: Boolean): Unit = {
    val totalLength = readRoadsAndCalculateTotalLength(osmOrcFile, clusterMode)
    println(s"Total length: $totalLength kms")
  }

  private def readRoadsAndCalculateTotalLength(osmOrcFile: String, clusterMode: Boolean) = {
    // 1. Create spark Session
    val sparkSession: SparkSession = SparkSession.builder()
      .master("local") // TODO take clusterMode into account later
      .getOrCreate()

    // 2. Read map data
    val osmFrame: DataFrame = sparkSession
      .read
      .orc(osmOrcFile)

    // 3. Calculate the length

    // 3.1 Read ways with node ids
    osmFrame.printSchema()
    val rawRoads = osmFrame
      .filter(column("type") === "way")
      .filter(
        array_contains(
          map_keys(column("tags")),
          "highway"
        )
      )
      .select(
        column("id"),
        posexplode(column("nds.ref")) as Seq("nodeIdx", "nodeId")
      )

    // 3.2 Read nodes
    val nodes = osmFrame
      .filter(column("type") === "node")
      .select(
        column("id") as "nodeId",
        column("lat"),
        column("lon")
      )

    // 3.3 Join ways and nodes
    val roadsWithNodes = rawRoads.join(nodes, "nodeId")
      .groupBy(column("id"))
      .agg(
        collect_list(
          struct(
            column("nodeId"),
            column("nodeIdx"),
            column("lat"),
            column("lon")
          )
        ) as "nodes"
      ).as[Road](Encoders.product[Road])

    // 3.4 Calculate total length
    roadsWithNodes
      .map { road =>
        val geometry: Seq[Point] = road.nodes
          .sortBy(_.nodeIdx)
          .map { case Node(_, _, lat, lon) => Point(lon.doubleValue(), lat.doubleValue()) }
        BigDecimal(LengthCalculator.calculateLength(geometry))
      }(sparkSession.implicits.newScalaDecimalEncoder)
      .reduce((way1Length, way2Length) => way1Length + way2Length) / 1000
  }


  case class Road(id: Long, nodes: Seq[Node])

  case class Node(nodeId: Long, nodeIdx: Int, lat: BigDecimal, lon: BigDecimal)

}
