package com.intellias.education.map.reader

import com.intellias.education.map.model.Way
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader

import java.nio.file.Paths

class GraphOnlyReader(pbfFile: String, parallelism: Int) extends EntityReader {

  def read: Seq[Way] = {
    val pbfReader = new PbfReader(Paths.get(pbfFile).toFile, parallelism)
    val sink = new RoadModelSink
    pbfReader.setSink(sink)
    pbfReader.run()
    sink.ways
  }

}
