package com.intellias.education.map.geometry.dist

class Spheroid(val semiMajorAxis: Double, val semiMinorAxis: Double, val flattening: Double) {

  def this(semiMajorAxis: Double, semiMinorAxis: Double) =
    this(semiMajorAxis, semiMinorAxis, (semiMajorAxis - semiMinorAxis) / semiMajorAxis)

  val eccentricitySquared: Double = {
    val a = semiMajorAxis
    val b = semiMinorAxis
    (a * a / (b * b)) - 1
  }

}

object Spheroids {
  object WGS_84 extends Spheroid(6378137.0, 6356752.3142)
}
