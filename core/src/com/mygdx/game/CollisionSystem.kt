package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
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
import com.mygdx.game.PlayerInputListener.Direction.DOWN
import com.mygdx.game.PlayerInputListener.Direction.LEFT
import com.mygdx.game.PlayerInputListener.Direction.RIGHT
import com.mygdx.game.PlayerInputListener.Direction.UP
import ktx.ashley.allOf
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y

internal class CollisionComponent : Component {
  // The rectangle to use for collision. Actor.updateRect can help.
  internal var boundingRect = Rectangle()

  var lastCollTime = 0L

  var lastMapObjColl: MapObject? = null
    set(value) {
      lastCollTime = TimeUtils.millis()
      field = value
    }

  // When there's a collision, there may be a call to setPosition() to correct the overlap.
  // The movement system should look at this, maybe adjust.
  var correction = vec2()

}

class CollisionSystem(private val collisionLayer: MapLayer, private val hazardsLayer: MapLayer) :
  IteratingSystem(
    allOf(
      ActorComponent::class,
      MovementComponent::class,
      CollisionComponent::class
    ).get(),
    0
  ) {

  private val tempRect = Rectangle()
  private var newPos = vec2()

  private var lastLogged = 0L

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return

    val mov = Components.Movement.get(entity) ?: return
    val coll = Components.Collision.get(entity) ?: return
    val actor = Components.Actor.get(entity)?.actor ?: return

    handleCollisions(coll, actor, mov)
    handleHazards(coll)

    if (TimeUtils.timeSinceMillis(lastLogged) <= 3000) return

    logger.info("corrective movement is from ${actor.pos} to $newPos")
    logger.info(
      "collision? actor: ${coll.boundingRect} into $tempRect - overlap? ${
        tempRect.overlaps(
          coll.boundingRect
        )
      }"
    )

    lastLogged = TimeUtils.millis()
  }

  private fun handleHazards(coll: CollisionComponent) {
    val collidingHazard =
      hazardsLayer.objects.find { obj -> obj is RectangleMapObject && obj.rectangle.overlaps(coll.boundingRect) }
        ?: return

    coll.lastMapObjColl = collidingHazard
  }

  private fun handleCollisions(
    coll: CollisionComponent,
    actor: DungeonActor,
    mov: MovementComponent
  ) {
    // Before checking collisions, update the bounding rect to use actor's dimensions.
    coll.boundingRect = updateCollisionRect(actor, coll.boundingRect)

    val collidingMapObj = collisionLayer.objects.find {
      // We use rectangles for collision and this makes this process a bit easier.
      if (it is RectangleMapObject) it.rectangle.overlaps(coll.boundingRect) else false
    } ?: return

    coll.lastMapObjColl = collidingMapObj
    coll.correction = getCorrection(mov, actor, collidingMapObj)
  }

  private fun getCorrection(
    mov: MovementComponent,
    actor: DungeonActor,
    collidingMapObj: MapObject
  ): Vector2 {
    val correction: Vector2 = when (mov.direction) {
      UP -> vec2(actor.x, collidingMapObj.y - (actor.height + 1))
      RIGHT -> vec2(collidingMapObj.x - (actor.width + 1), actor.y)
      DOWN -> vec2(actor.x, collidingMapObj.y + collidingMapObj.height + 1)
      LEFT -> vec2(collidingMapObj.x + collidingMapObj.width + 1, actor.y)
      else -> return Vector2.Zero
    }
    return if (correction != actor.pos) correction else correction.setZero()
  }

  companion object {
    val logger: Logger = Logger("coll", INFO)
  }

  private fun updateCollisionRect(actor: Actor, rect: Rectangle): Rectangle =
    rect.set(actor.x, actor.y, actor.width, actor.height)
}