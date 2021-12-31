package com.intellias.education.map

import com.intellias.education.map.model.Way

class RoadNetworkOperations(ways: Seq[Way]) {

  def totalLength: BigDecimal = ways.map(_.length).sum

}
