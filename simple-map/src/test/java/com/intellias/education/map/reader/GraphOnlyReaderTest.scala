package com.intellias.education.map.reader

import org.scalatest.flatspec.AnyFlatSpec

import java.nio.file.Paths

class GraphOnlyReaderTest extends AnyFlatSpec {

  private val testPbfFile = {
    val url = getClass.getClassLoader.getResource("luxembourg-test.osm.pbf")
    Paths.get(url.toURI).toAbsolutePath.toString
  }

  "Graph Reader" should "read ways" in {
    val reader = new GraphOnlyReader(testPbfFile, 10)
    val ways = reader.read

    assert(ways.size == 117888)
  }

}
