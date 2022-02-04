package com.intellias.education.map.reader

import com.intellias.education.map.model.{Node, Way}
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer
import org.openstreetmap.osmosis.core.domain.v0_6.{EntityType, Node => OsmNode, Way => OsmWay}
import org.openstreetmap.osmosis.core.task.v0_6.Sink

import java.util
import java.util.concurrent.atomic.AtomicBoolean
import scala.collection.JavaConverters.{asScalaBufferConverter, iterableAsScalaIterableConverter}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class RoadModelSink extends Sink {

  private val isCompleted = new AtomicBoolean(false)

  private val nodesCacheById: collection.mutable.Map[Long, Node] = new mutable.HashMap[Long, Node]
  private val waysBuffer: collection.mutable.Buffer[Way] = new ListBuffer[Way]

  def ways: Seq[Way] = if (isCompleted.get()) waysBuffer.toList else throw new IllegalStateException

  override def initialize(metaData: util.Map[String, AnyRef]): Unit = println(s"OSM file metadata: $metaData")

  override def process(entityContainer: EntityContainer): Unit = {
    val entity = entityContainer.getEntity
    entity.getType match {
      case EntityType.Node =>
        val osmNode = entity.asInstanceOf[OsmNode]
        nodesCacheById += entity.getId -> Node(entity.getId, osmNode.getLongitude, osmNode.getLatitude)

      case EntityType.Way =>
        val osmWay = entity.asInstanceOf[OsmWay]
        val tags = entity.getTags
        if (tags.asScala.exists(_.getKey == "highway")) {
          val nodes = osmWay.getWayNodes.asScala
            .map(n => nodesCacheById(n.getNodeId))
          waysBuffer += Way(osmWay.getId, nodes)
        }
      case _ =>
    }
  }

  override def complete(): Unit = isCompleted.set(true)

  override def close(): Unit = {}

}
