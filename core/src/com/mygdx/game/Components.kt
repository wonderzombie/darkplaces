package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.PlayerInputListener.Direction
import com.mygdx.game.TypeComponent.Type.UNSET
import ktx.ashley.mapperFor
import ktx.math.vec2

class Components {
  companion object {
    internal val Collision = mapperFor<CollisionComponent>()
    internal val Player = mapperFor<PlayerComponent>()
    internal val Animation = mapperFor<AnimationComponent>()
    internal val State = mapperFor<StateComponent>()
    internal val Type = mapperFor<TypeComponent>()
    internal val Actor = mapperFor<ActorComponent>()
    internal val Movement = mapperFor<MovementComponent>()
  }
}

internal class PlayerComponent : Component

internal class MovementComponent : Component {
  // The current direction. Setting this will update lastDirection and lastMoved.
  var direction: Direction = Direction.NONE
    set(value) {
      lastDirection = field
      field = value
      if (direction != Direction.NONE) {
        lastMoved = TimeUtils.millis()
      }
    }
  var lastMoved: Long = 0L
  var lastDirection: Direction = Direction.NONE

  // The distance this entity should move.
  var x = 0f
  var y = 0f

  // How quickly that move should happen.
  var duration = 0.01f
  var interp: Interpolation = Interpolation.fastSlow

  // If the actor has a movement action, it may be here.
  var currentMovement: MoveByAction? = null
}

private val actorLogger = Logger("actor", INFO)

class DungeonActor : Actor() {
  private var id: String = TimeUtils.millis().toString()
  private var stateTime: Float = 0f

  override fun act(delta: Float) {
    stateTime += delta
    if (TimeUtils.millis() % 1000L == 0L) {
      actorLogger.info("acting acting")
    }
    super.act(delta)
  }

  fun upateRect(rect: Rectangle): Rectangle =
    rect.let { it.set(x, y, width, height) }

  val pos: Vector2 = vec2()
    get() = field.set(this.x, this.y)
}

internal class ActorComponent : Component {
  lateinit var actor: DungeonActor

}

internal class AnimationComponent : Component {
  var stateTime: Float = 0f

  var idle: Animation<AtlasRegion>? = null
  var moving: Animation<AtlasRegion>? = null
}

internal class TypeComponent : Component {
  enum class Type {
    UNSET,
    PLAYER,
    MONSTER,
  }

  var type: Type = UNSET
}

internal class StateComponent : Component {
  enum class State {
    UNSET,
    IDLE,
    MOVING,
    DEAD,
  }

  var stateTime = 0L

  var state = State.UNSET
    set(value) {
      stateTime = 0L
      field = value
    }
}

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
