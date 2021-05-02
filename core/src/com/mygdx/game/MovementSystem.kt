package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.Components.Companion.Actor
import com.mygdx.game.Components.Companion.State
import com.mygdx.game.MovementSystem.Direction
import com.mygdx.game.MovementSystem.Direction.NONE
import com.mygdx.game.StateComponent.State.IDLE
import com.mygdx.game.StateComponent.State.MOVING
import ktx.ashley.allOf
import ktx.ashley.contains

internal class MovementComponent : Component {
  // The current direction. Setting this will update lastDirection and lastMoved.
  var direction: Direction = Direction.NONE
    set(value) {
      lastDirection = field
      field = value
      if (direction != Direction.NONE) {
        lastMoved = TimeUtils.millis()
      }
    }
  var lastMoved: Long = 0L
  var lastDirection: Direction = Direction.NONE

  // The distance this entity should move per frame (stage coordinates).
  var x = 0f
  var y = 0f

  var interp: Interpolation = Interpolation.fastSlow

  // If the actor has a movement action, it may be here.
  var currentMovement: MoveByAction? = null
}

class MovementSystem :
  IteratingSystem(allOf(MovementComponent::class).get(), EnginePriority.Movement) {
  private val logger: Logger = Logger("mov", INFO)

  private fun stop(mov: MovementComponent, stateComp: StateComponent) {
    logger.info("movement halted")
    stateComp.state = IDLE
    mov.direction = NONE
    mov.currentMovement?.finish()
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    if (Actor !in entity || State !in entity) return

    val actor = entity.actor() ?: return
    val mov = entity.movComp() ?: return
    val stateComp = entity.stateComp() ?: return
    val coll = entity.collComp()

    val shouldStop = isCollisionPending(coll)
        || isMoveActionComplete(mov)

    if (shouldStop) {
      stop(mov, stateComp)
      actor.also { "position: ${it.x},${it.y}" }
      maybeApplyCorrection(coll, actor)
      return
    }

    mov.let {
      it.currentMovement =
        Actions.moveBy(
          it.direction.x * it.x * deltaTime,
          it.direction.y * it.y * deltaTime,
          0.01f,
          it.interp
        )
      actor.addAction(mov.currentMovement)
    }
  }

  private fun isMoveActionComplete(mov: MovementComponent?): Boolean =
    mov?.currentMovement?.isComplete ?: false

  private fun isCollisionPending(coll: CollisionComponent?): Boolean =
    coll?.let { !it.correction.isZero } ?: false

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

  enum class Direction(internal val x: Float, internal val y: Float, internal val rotation: Float) {
    NONE(0f, 0f, 0f),
    UP(0f, 1f, 0f),
    RIGHT(1f, 0f, 90f),
    DOWN(0f, -1f, 180f),
    LEFT(-1f, 0f, 270f);

    val isCardinal: Boolean
      get() = this != NONE
  }
}