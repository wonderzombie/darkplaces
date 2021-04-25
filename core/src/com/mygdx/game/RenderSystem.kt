package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.MovementSystem.Direction.RIGHT
import com.mygdx.game.StateComponent.State.MOVING
import ktx.ashley.allOf

class RenderSystem(private val batch: SpriteBatch, family: Family = RenderSystem.family) :
  IteratingSystem(family, EnginePriority.Render) {

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    val actorComp = Components.Actor.get(entity) ?: return
    val stateComp = Components.State.get(entity) ?: return
    val animComp = Components.Animation.get(entity) ?: return
    val movComp = Components.Movement.get(entity) ?: return

    animComp.stateTime += deltaTime

    val animGroup = when (stateComp.state) {
      MOVING -> animComp.moving
      else -> animComp.idle
    }

    val animWithDirection = animGroup[movComp.direction] ?: animGroup[RIGHT]
    check(animWithDirection != null)

    animWithDirection.let {
      batch.begin()
      with(actorComp.actor) {
        batch.draw(
          it.getKeyFrame(animComp.stateTime),
          x, y, originX, originY, width, height, scaleX, scaleY, rotation
        )
      }
      batch.end()
    }
  }

  companion object {
    val family: Family =
      allOf(
        ActorComponent::class,
        AnimationComponent::class,
        StateComponent::class,
        MovementComponent::class,
      ).get()
  }
}