package com.intellias.education.map.geometry.dist

import scala.math._

class BowringDistance(spheroid: Spheroid) extends Distance {

  override def distance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double = {
    val a = spheroid.semiMajorAxis
    val eprimeᵗʷᵒ = spheroid.eccentricitySquared

    val φ1 = toRadians(lat1)
    val φ2 = toRadians(lat2)
    val λ1 = toRadians(lon1)
    val λ2 = toRadians(lon2)

    val Δφ = φ2 - φ1
    val Δλ = λ2 - λ1

    val cosφ1 = cos(φ1)
    val A = sqrt(1 + eprimeᵗʷᵒ * Math.pow(cosφ1, 4))
    val B = sqrt(1 + eprimeᵗʷᵒ * Math.pow(cosφ1, 2))
    val C = sqrt(1 + eprimeᵗʷᵒ)

    val w = A * Δλ / 2
    val t1 = 3 * eprimeᵗʷᵒ * Δφ * sin(2 * φ1 + 2.0 / 3.0 * Δφ) / (4 * B * B)
    val D = Δφ / (2 * B) * (1 + t1)
    val sinD = sin(D)
    val E = sinD * cos(w)
    val t2 = B * cosφ1 * cos(D) - sin(φ1) * sinD
    val F = 1 / A * sin(w) * t2
    val σ = 2 * asin(sqrt(E * E + F * F))
    a * C * σ / (B * B)
  }

}

object BowringDistance extends BowringDistance(Spheroids.WGS_84)
