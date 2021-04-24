package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
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

internal data class AnimationComponent(
  var stateTime: Float = 0f,
  var idle: Animation<AtlasRegion>? = null,
  var moving: Animation<AtlasRegion>? = null
) : Component

internal data class TypeComponent(var type: Type = UNSET) : Component {
  enum class Type {
    UNSET,
    PLAYER,
    MONSTER,
  }
}

internal data class StateComponent(var stateTime: Long = 0L, var state: State = State.UNSET) :
  Component {
  enum class State {
    UNSET,
    IDLE,
    MOVING,
    DEAD,
  }

  fun update(newState: State): StateComponent {
    state = newState
    stateTime = 0L
    return this
  }
}