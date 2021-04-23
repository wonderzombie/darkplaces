package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf

class RenderSystem(private val batch: SpriteBatch, family: Family = RenderSystem.family) :
  IteratingSystem(family, 1) {

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return

    val animComp = Components.Animation.get(entity)
    val actorComp = Components.Actor.get(entity)

    val anim = with(animComp) {
      stateTime += deltaTime
      if (animation.keyFrames.isNotEmpty()) animation else texAnimation
    }

    batch.begin()
    with(actorComp.actor) {
      batch.draw(
        anim.getKeyFrame(animComp.stateTime),
        x, y, originX, originY, width, height, scaleX, scaleY, rotation
      )
    }
    batch.end()
  }

  companion object {
    val family: Family =
      allOf(
        ActorComponent::class,
        AnimationComponent::class
      ).get()
  }
}