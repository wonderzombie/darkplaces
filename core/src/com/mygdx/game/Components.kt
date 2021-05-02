package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.MovementSystem.Direction
import com.mygdx.game.TypeComponent.Type.UNSET
import ktx.ashley.mapperFor

class Components {
  companion object {
    internal val Player = mapperFor<PlayerComponent>()
    internal val Actor = mapperFor<ActorComponent>()
    internal val Attack = mapperFor<AttackComponent>()
    internal val Animation = mapperFor<RenderComponent>()
    internal val Collision = mapperFor<CollisionComponent>()
    internal val Combat = mapperFor<CombatComponent>()
    internal val Movement = mapperFor<MovementComponent>()
    internal val State = mapperFor<StateComponent>()
    internal val Type = mapperFor<TypeComponent>()
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

data class AttackComponent(
  val id: String = "Attack|${TimeUtils.millis()}",
  var name: String = "",
  var damage: Int = 0,
  var started: Long = 0L,
  var actor: DungeonActor? = null,
  var boundingRect: Rectangle? = null,
  var cooldownMs: Long = 0,
) : Component {
  var active: Boolean = false
    set(value) {
      started = TimeUtils.millis()
      field = value
    }
}

typealias AtlasAnim = Animation<AtlasRegion>

data class RenderComponent(
  var stateTime: Float = 0f,
  var idle: Map<Direction, AtlasAnim> = mapOf(),
  var moving: Map<Direction, AtlasAnim> = mapOf(),
  var tex: AtlasRegion? = null
) : Component {
}

data class TypeComponent(var type: Type = UNSET, var subtype: String = "") : Component {
  enum class Type {
    UNSET,
    PLAYER,
    MONSTER,
    ATTACK;
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
    ATTACK,
  }

  var state: State = State.UNSET
    set(value) {
      field = value
      stateTime = TimeUtils.millis()
    }
}