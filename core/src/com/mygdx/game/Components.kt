package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.mygdx.game.TypeComponent.Type.UNSET
import ktx.ashley.mapperFor

class Components {
  companion object {
    internal val Collision = mapperFor<CollisionComponent>()
    internal val Player = mapperFor<PlayerComponent>()
    internal val Animation = mapperFor<AnimationComponent>()
    internal val State = mapperFor<StateComponent>()
    internal val Type = mapperFor<TypeComponent>()
    internal val Actor = mapperFor<ActorComponent>()
    internal val Movement = mapperFor<MovementComponent>()
    internal val Combat = mapperFor<CombatComponent>()
  }
}

internal class PlayerComponent : Component

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

internal data class StateComponent(
  var stateTime: Long = 0L,
  private var presentState: State = State.UNSET
) :
  Component {
  enum class State {
    UNSET,
    IDLE,
    MOVING,
    HIT,
    DEAD,
  }

  var state: State = presentState
    set(value) {
      field = value
      stateTime = 0L
    }
}