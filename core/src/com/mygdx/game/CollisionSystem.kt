package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.MovementSystem.Direction.DOWN
import com.mygdx.game.MovementSystem.Direction.LEFT
import com.mygdx.game.MovementSystem.Direction.RIGHT
import com.mygdx.game.MovementSystem.Direction.UP
import com.mygdx.game.constants.Assets.Constants.Companion.ZeroRect
import ktx.ashley.allOf
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y
import java.util.*

internal class CollisionComponent : Component {
  // The rectangle to use for collision. Actor.updateRect can help.
  internal var boundingRect = Rectangle()

  var lastCollTime = 0L

  var lastMapObjColl: MapObject? = null
    set(value) {
      if (value != null) lastCollTime = TimeUtils.millis()
      field = value
    }

  // When there's a collision, there may be a call to setPosition() to correct the overlap.
  // A hazard might not. The movement system may look at this and adjust the position to these coords.
  var correction = vec2()
}

class CollisionSystem(private val collisionLayer: MapLayer, private val hazardsLayer: MapLayer) :
  IteratingSystem(
    allOf(
      ActorComponent::class,
      MovementComponent::class,
      TypeComponent::class,
      CollisionComponent::class
    ).get(),
    EnginePriority.Collision
  ) {

  private val tempActorRect = Rectangle()
  private val tempOtherRect = Rectangle()

  private var sortedXList: SortedMap<Float, Entity> = sortedMapOf()
  private var sortedYList: SortedMap<Float, Entity> = sortedMapOf()

  private var newPos = vec2()

  private var lastLoggedActorColl: Long = 0L
  private var lastLoggedProcessEntity = 0L

  private fun Rectangle.toZero() {
    this.set(0f, 0f, 0f, 0f)
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    setProcessing(true)

    sortedXList = sortedXList.values.associateBy { it.actorComp()?.actor?.x ?: 0f }.toSortedMap()
    sortedYList = sortedYList.values.associateBy { it.actorComp()?.actor?.y ?: 0f }.toSortedMap()

    val mov = entity.movComp() ?: return
    val coll = entity.collComp() ?: return
    val actor = entity.actorComp()?.actor ?: return
    val typeComp = entity.typeComp() ?: return

    coll.boundingRect = updateCollisionRect(actor, coll.boundingRect)

    handleObjectCollisions(coll, actor, mov)
    handleHazards(coll)
    handleActorCollisions(coll, actor)

    setProcessing(false)
    if (TimeUtils.timeSinceMillis(lastLoggedProcessEntity) <= 3000) return
    logger.info("any collisions? ${coll.correction.isZero}")
    lastLoggedProcessEntity = TimeUtils.millis()
  }

  private fun handleActorCollisions(
    coll: CollisionComponent,
    actor: DungeonActor,
  ) {
    actor.updateRect(coll.boundingRect)
    tempOtherRect.toZero()
    // go through all actors, update them into a temp rect, and compare with current actor for overlap

    if (actor.x !in sortedXList && actor.right !in sortedXList) return
    if (actor.y !in sortedYList && actor.top !in sortedYList) return

    val xCollisions = sortedXList.headMap(actor.x).tailMap(actor.right)
    val yCollisions = sortedYList.headMap(actor.y).tailMap(actor.top)

    val trueCollisions = xCollisions.values.filter { it in yCollisions.values }
    if (TimeUtils.timeSinceMillis(lastLoggedActorColl) >= 3000) return

    logger.info(
      "actor collisions? ${trueCollisions.size} - ${
        trueCollisions.map { it.typeComp()?.type }.joinToString("|")
      }"
    )

    this.lastLoggedActorColl = TimeUtils.millis()
  }

  private fun handleHazards(coll: CollisionComponent) {
    val collidingHazard =
      hazardsLayer.objects.find { it is RectangleMapObject && it.rectangle.overlaps(coll.boundingRect) }
    coll.lastMapObjColl = collidingHazard
  }

  private fun handleObjectCollisions(
    coll: CollisionComponent,
    actor: DungeonActor,
    mov: MovementComponent
  ) {
    val collidingMapObj = collisionLayer.objects.find {
      // We use rectangles for collision and this makes this process a bit easier.
      it is RectangleMapObject && it.rectangle.overlaps(coll.boundingRect)
    } ?: return

    coll.lastMapObjColl = collidingMapObj as RectangleMapObject
    coll.correction = getCorrection(mov, actor, collidingMapObj)
  }

  private fun getCorrection(
    mov: MovementComponent,
    actor: DungeonActor,
    collidingMapObj: MapObject
  ): Vector2 {
    val retVec = vec2()
    val correction: Vector2 = when (mov.direction) {
      UP -> retVec.set(actor.x, collidingMapObj.y - (actor.height + 1))
      RIGHT -> retVec.set(collidingMapObj.x - (actor.width + 1), actor.y)
      DOWN -> retVec.set(actor.x, collidingMapObj.y + collidingMapObj.height + 1)
      LEFT -> retVec.set(collidingMapObj.x + collidingMapObj.width + 1, actor.y)
      else -> return retVec.setZero()
    }
    return if (correction != actor.pos) correction else correction.setZero()
  }

  companion object {
    val logger: Logger = Logger("coll", INFO)
  }

  private fun updateCollisionRect(actor: Actor, rect: Rectangle?): Rectangle =
    rect?.set(actor.x, actor.y, actor.width, actor.height) ?: ZeroRect

  inner class CollisionListener : EntityListener {
    override fun entityAdded(entity: Entity?) {
      val actor = entity?.actorComp()?.actor ?: return

      sortedXList[actor.x] = entity
      sortedXList[actor.right] = entity

      sortedYList[actor.y] = entity
      sortedYList[actor.top] = entity
    }

    override fun entityRemoved(entity: Entity?) {
      TODO("Not yet implemented")
    }
  }

  private fun Actor.updateRect(tempRect: Rectangle): Rectangle {
    return tempRect.set(x, y, width, height)
  }
}