package com.mygdx.game

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.Logger
import ktx.ashley.allOf
import ktx.ashley.get

class ActorSystem : IteratingSystem(allOf(ActorComponent::class).get()) {
  private val logger: Logger = Logger("sys-actor")

  override fun addedToEngine(engine: Engine?) {
    engine?.addEntityListener(ActorListener())
    super.addedToEngine(engine)
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    entity[Components.Actor]?.actor ?: return
  }


  inner class ActorListener : EntityListener {
    override fun entityAdded(entity: Entity?) {
      logger.info("entityAdded: $entity")
    }

    override fun entityRemoved(entity: Entity?) {
      entity ?: return
    }
  }
}