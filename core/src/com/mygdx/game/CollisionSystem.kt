package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
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
      CollisionComponent::class,
      MovementComponent::class,
      TypeComponent::class,
    ).get(),
    EnginePriority.Collision
  ) {

  private var sortedXList: SortedMap<Float, Entity> = sortedMapOf()
  private var sortedYList: SortedMap<Float, Entity> = sortedMapOf()

  private var lastLoggedActorColl: Long = 0L
  private var lastLoggedProcessEntity = 0L

  private fun Rectangle.toZero() {
    this.set(0f, 0f, 0f, 0f)
  }


  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return

    sortedXList = sortedXList.values.associateBy { it.actorComp()?.actor?.x ?: 0f }.toSortedMap()
    sortedYList = sortedYList.values.associateBy { it.actorComp()?.actor?.y ?: 0f }.toSortedMap()

    val mov = entity.movComp() ?: return
    val coll = entity.collComp() ?: return
    val actor = entity.actor() ?: return

    coll.boundingRect = updateCollisionRect(actor, coll.boundingRect)

    handleObjectCollisions(coll, actor, mov)
    handleHazards(coll)
    handleActorCollisions(entity, coll, actor)

    if (TimeUtils.timeSinceMillis(lastLoggedProcessEntity) <= 3000) return
    logger.info("object collisions? ${!coll.correction.isZero}")
    lastLoggedProcessEntity = TimeUtils.millis()
  }

  private fun handleActorCollisions(
    entity: Entity,
    coll: CollisionComponent,
    actor: DungeonActor,
  ) {
    if (actor.x !in sortedXList && actor.right !in sortedXList) return
    if (actor.y !in sortedYList && actor.top !in sortedYList) return

    val xCollEntities = sortedXList.subMap(actor.x, actor.right).values.apply { remove(entity) }
    val yCollEntities = sortedYList.subMap(actor.y, actor.top).values.apply { remove(entity) }

    if (xCollEntities.isEmpty() && yCollEntities.isEmpty()) return

    val testedCollision = xCollEntities.filter(yCollEntities::contains)
      .filter { it?.collComp()?.boundingRect?.overlaps(coll.boundingRect) == true }

    if (TimeUtils.timeSinceMillis(lastLoggedActorColl) >= 1500) return

    logger.info(
      "actor collisions? ${testedCollision.size} - ${testedCollision}"
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


  private fun Actor.updateRect(tempRect: Rectangle): Rectangle {
    return tempRect.set(x, y, width, height)
  }

  override fun addedToEngine(engine: Engine?) {
    engine?.addEntityListener(CollisionListener())
    super.addedToEngine(engine)
  }

  inner class CollisionListener : EntityListener {
    override fun entityAdded(entity: Entity?) {
      val actor = entity?.actorComp()?.actor ?: return
      sortedXList[actor.x] = entity
      sortedXList[actor.right] = entity

      sortedYList[actor.y] = entity
      sortedYList[actor.top] = entity

      entity.collComp()?.boundingRect?.apply { actor.updateRect(this) }
    }

    override fun entityRemoved(entity: Entity?) {
      TODO("Not yet implemented")
    }
  }
}