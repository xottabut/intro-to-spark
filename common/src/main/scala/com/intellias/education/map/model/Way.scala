package com.intellias.education.map.model

import com.intellias.education.map.geometry.dist.LengthCalculator

case class Way(id: Long, nodes: Seq[Node] = Seq.empty) {

  lazy val length: Double = geometryLength

  val geometry: Seq[Point] = nodes.map(_.coordinate)

  def hasNodes: Boolean = nodes.nonEmpty

  private def geometryLength: Double = LengthCalculator.calculateLength(geometry)

}
