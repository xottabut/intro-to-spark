package com.intellias.education.map.geometry.dist

import com.intellias.education.map.model.Point

object LengthCalculator {

  private val distance: Distance = BowringDistance

  def calculateLength(geometry: Seq[Point]): Double = geometry.sliding(2)
    .map { case Seq(p1, p2) => distance.distance(p1, p2) }
    .sum

}
