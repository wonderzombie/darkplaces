package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.MovementSystem.Direction
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

data class PlayerComponent(
  var id: String = "P|${TimeUtils.millis()}",
  var name: String = ""
) : Component

data class EnemyComponent(
  var id: String = "E|${TimeUtils.millis()}",
  var name: String = "",
) : Component

typealias AtlasAnim = Animation<AtlasRegion>

data class AnimationComponent(
  var stateTime: Float = 0f,
  var idle: Map<Direction, AtlasAnim> = mapOf(),
  var moving: Map<Direction, AtlasAnim> = mapOf(),
) : Component

data class TypeComponent(var type: Type = UNSET, var subtype: String = "") : Component {
  enum class Type {
    UNSET,
    PLAYER,
    MONSTER;
  }

  val isPlayer: Boolean
    get() = type == Type.PLAYER

  val isMonster: Boolean
    get() = type == Type.MONSTER

}

data class StateComponent(
  var stateTime: Long = 0L,
) :
  Component {
  enum class State {
    UNSET,
    IDLE,
    MOVING,
    HIT,
    DEAD,
  }

  var state: State = State.UNSET
    set(value) {
      field = value
      stateTime = TimeUtils.millis()
    }
}