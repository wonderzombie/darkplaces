package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.TypeComponent.Type.UNSET
import ktx.ashley.mapperFor


class Components {
  companion object {
    internal val Player = mapperFor<PlayerComponent>()
    internal val Animation = mapperFor<AnimationComponent>()
    internal val State = mapperFor<StateComponent>()
    internal val Type = mapperFor<TypeComponent>()
    internal val Actor = mapperFor<ActorComponent>()
  }
}

internal class PlayerComponent : Component

internal class ActorComponent(val actor: Actor = Actor()) : Component

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
