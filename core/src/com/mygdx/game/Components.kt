package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.StageInputListener.Direction
import com.mygdx.game.TypeComponent.Type.UNSET
import ktx.ashley.mapperFor

class Components {
  companion object {
    internal val Player = mapperFor<PlayerComponent>()
    internal val Animation = mapperFor<AnimationComponent>()
    internal val State = mapperFor<StateComponent>()
    internal val Type = mapperFor<TypeComponent>()
    internal val Actor = mapperFor<ActorComponent>()
    internal val Movement = mapperFor<MovementComponent>()
  }
}

internal class PlayerComponent : Component {}

internal class MovementComponent : Component {
  var lastDirection: Direction = Direction.NONE
  var x = 16f
  var y = 16f
  var duration = 0.1f
  var interp = Interpolation.fastSlow
  var stateTime = 0f
}

private val actorLogger = Logger("actor", INFO)

class DungeonActor : Actor() {
  private var id: Long = TimeUtils.millis()
  private var stateTime: Float = 0f

  override fun act(delta: Float) {
    stateTime += delta
    if (TimeUtils.millis() % 1000L == 0L) {
      actorLogger.info("acting acting")
    }
    super.act(delta)
  }
}

internal class ActorComponent : Component {
  lateinit var actor: DungeonActor
}

internal class AnimationComponent : Component {
  var stateTime: Float = 0f

  lateinit var animation: Animation<AtlasRegion>
  lateinit var texAnimation: Animation<TextureRegion>
}

internal class TypeComponent : Component {
  enum class Type {
    UNSET,
    PLAYER,
    MONSTER,
    NPC
  }

  var type: Type = UNSET
}

internal class StateComponent : Component {
  enum class State {
    UNSET,
    IDLE,
    MOVING,
    DEAD,
  };

  var stateTime = 0f

  var state = State.UNSET
    set(value) {
      stateTime = 0f
      field = value
    }
}
