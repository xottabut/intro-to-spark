package com.intellias.education.map.model

case class Node(id: Long, lon: Double, lat: Double) {

  val coordinate: Point = Point(lon, lat)

}
