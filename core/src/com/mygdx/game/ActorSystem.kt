package com.mygdx.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.Logger
import ktx.ashley.allOf

class ActorSystem : IteratingSystem(
  allOf(ActorComponent::class, StateComponent::class, TypeComponent::class).get(),
  0
) {
  private val logger: Logger = Logger("sys-actor", Logger.INFO)

  override fun addedToEngine(engine: Engine?) {
    engine?.addEntityListener(ActorListener())
    super.addedToEngine(engine)
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
  }

  inner class ActorListener : EntityListener {
    override fun entityAdded(entity: Entity?) {
      entity ?: return
    }

    override fun entityRemoved(entity: Entity?) {
      entity ?: return
    }
  }
}