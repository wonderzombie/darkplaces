package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.mygdx.game.MovementSystem.Direction
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
    val movComp = Components.Movement.get(entity)

    animComp.stateTime += deltaTime

    // short-circuit on just a texture
    if (animComp.tex != null) return renderTexture(actorComp.actor, animComp.tex)

    if (!actorComp.actor.isVisible) logger.info("skipped rendering somebody").also { return }

    val animGroup = when (stateComp.state) {
      MOVING -> animComp.moving
      else -> animComp.idle
    }

    val directionalAnim = movComp?.let {
      if (it.direction == Direction.NONE) {
        if (it.lastDirection.isCardinal) {
          animGroup[it.lastDirection]
        } else {
          animGroup[RIGHT]
        }
      }
      animGroup[it.direction]
    } ?: animComp.idle.values.firstOrNull()

//    val animWithDirection: AtlasAnim? = movComp?.let {
//      animGroup[it.direction] ?: animGroup[RIGHT]
//    } ?: animComp.idle.values.firstOrNull()

    check(directionalAnim != null) { "unable to find animation for entity $entity" }

    draw(actorComp, directionalAnim, animComp)
  }

  private fun draw(
    actorComp: ActorComponent,
    frame: AtlasAnim,
    animComp: RenderComponent
  ) {
    batch.begin()
    with(actorComp.actor) {
      batch.draw(
        frame.getKeyFrame(animComp.stateTime),
        x, y, originX, originY, width, height, scaleX, scaleY, rotation
      )
    }
    batch.end()
  }

  private fun renderTexture(actor: DungeonActor, tex: AtlasRegion?) {
    batch.begin()
    with(actor) {
      batch.draw(
        tex,
        x, y, originX, originY, width, height, scaleX, scaleY, rotation
      )
    }
    batch.end()
  }

  companion object {
    val family: Family =
      allOf(
        ActorComponent::class,
        RenderComponent::class,
        StateComponent::class,
        MovementComponent::class,
      ).get()
  }
}