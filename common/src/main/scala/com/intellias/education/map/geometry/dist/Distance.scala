package com.intellias.education.map.geometry.dist

import com.intellias.education.map.model.Point

trait Distance {

  def distance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double

  def distance(p1: Point, p2: Point): Double = distance(p1.x, p1.y, p2.x, p2.y)

}
