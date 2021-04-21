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
    internal val playerMapper = mapperFor<PlayerComponent>()
    internal val animMapper = mapperFor<AnimationComponent>()
    internal val stateMapper = mapperFor<StateComponent>()
    internal val typeMapper = mapperFor<TypeComponent>()
    internal val actorMapper = mapperFor<ActorComponent>()
  }
}

internal class PlayerComponent : Component

internal class ActorComponent : Component {
  var actor: Actor = Actor()
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
    NOMINAL,
    MOVING,
  };

  var stateTime = 0f

  var state = UNSET
    set(value) {
      stateTime = 0f
      field = value
    }
}
