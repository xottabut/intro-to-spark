package com.intellias.education.map.geometry.dist

import scala.math._

class BowringDistance(spheroid: Spheroid) extends Distance {

  override def distance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double = {
    val a = spheroid.semiMajorAxis
    val ePrimeSq = spheroid.eccentricitySquared

    val phi1 = toRadians(lat1)
    val phi2 = toRadians(lat2)
    val lambda1 = toRadians(lon1)
    val lambda2 = toRadians(lon2)

    val deltaPhi = phi2 - phi1
    val deltaLambda = lambda2 - lambda1

    val cosPhi1 = cos(phi1)
    val A = sqrt(1 + ePrimeSq * Math.pow(cosPhi1, 4))
    val B = sqrt(1 + ePrimeSq * Math.pow(cosPhi1, 2))
    val C = sqrt(1 + ePrimeSq)

    val w = A * deltaLambda / 2
    val t1 = 3 * ePrimeSq * deltaPhi * sin(2 * phi1 + 2.0 / 3.0 * deltaPhi) / (4 * B * B)
    val D = deltaPhi / (2 * B) * (1 + t1)
    val sinD = sin(D)
    val E = sinD * cos(w)
    val t2 = B * cosPhi1 * cos(D) - sin(phi1) * sinD
    val F = 1 / A * sin(w) * t2
    val sigma = 2 * asin(sqrt(E * E + F * F))
    a * C * sigma / (B * B)
  }

}

object BowringDistance extends BowringDistance(Spheroids.WGS_84)
