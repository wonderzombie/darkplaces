package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.mygdx.game.Components.Companion.Actor
import com.mygdx.game.Components.Companion.Collision
import com.mygdx.game.Components.Companion.Movement
import com.mygdx.game.Components.Companion.State
import com.mygdx.game.PlayerInputListener.Direction.NONE
import com.mygdx.game.StateComponent.State.IDLE
import com.mygdx.game.StateComponent.State.MOVING
import ktx.ashley.allOf
import ktx.ashley.contains

class MovementSystem : IteratingSystem(allOf(MovementComponent::class).get(), 1) {
  private val logger: Logger = Logger("mov", INFO)

  private fun stop(mov: MovementComponent, stateComp: StateComponent) {
    stateComp.state = IDLE
    mov.direction = NONE
    mov.currentMovement?.finish()
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    if (Actor !in entity || State !in entity) return

    val actor = Actor.get(entity)?.actor
    val mov = Movement.get(entity)
    val coll = Collision.get(entity)
    val stateComp = State.get(entity)

    val shouldStop = hasPendingCollision(coll)
        || movActionComplete(mov)

    if (shouldStop) {
      stop(mov, stateComp)
      actor?.also { "position: ${it.x},${it.y}" }
      maybeApplyCorrection(coll, actor)
      return
    }

    if (mov?.direction == mov?.lastDirection && stateComp.state == MOVING) {
      mov?.let {
        it.currentMovement =
          Actions.moveBy(
            it.direction.x * it.x * deltaTime,
            it.direction.y * it.y * deltaTime,
            0.01f,
            it.interp
          )
      }
      actor?.addAction(mov?.currentMovement)
    }
  }

  private fun movActionComplete(mov: MovementComponent?) =
    mov?.currentMovement?.isComplete == true

  private fun hasPendingCollision(coll: CollisionComponent?) =
    coll?.correction?.isZero?.not() == true

  private fun maybeApplyCorrection(
    coll: CollisionComponent?,
    actor: DungeonActor?
  ) {
    if (coll?.correction == null || coll.correction.isZero || actor == null) return
    logger.info("correction: ${coll.correction}")
    actor.clearActions()
    actor.setPosition(coll.correction.x, coll.correction.y)
    coll.correction.setZero()
  }

}