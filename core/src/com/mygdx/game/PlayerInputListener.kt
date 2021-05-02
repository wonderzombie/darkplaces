package com.mygdx.game

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.MovementSystem.Direction.DOWN
import com.mygdx.game.MovementSystem.Direction.LEFT
import com.mygdx.game.MovementSystem.Direction.NONE
import com.mygdx.game.MovementSystem.Direction.RIGHT
import com.mygdx.game.MovementSystem.Direction.UP
import com.mygdx.game.StateComponent.State.IDLE
import com.mygdx.game.StateComponent.State.MOVING
import ktx.ashley.EngineEntity
import ktx.ashley.get

class PlayerInputListener(controlledEntity: EngineEntity) : InputListener() {
  private val logger: Logger = Logger("input", INFO)
  private val player = controlledEntity.entity

  private val defaultKeymap = mapOf(
    Keys.UP to UP,
    Keys.RIGHT to RIGHT,
    Keys.DOWN to DOWN,
    Keys.LEFT to LEFT
  )

  override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
    if (keycode == Keys.H) {
      logger.error("debugging if you want it")
    }
    logger.info("event $event - keycode $keycode")
//    if (keycode !in defaultKeymap) return false

    val actor = player.actorComp()?.actor
    val movementComp = player.movComp()
    val stateComp = player.stateComp()

    val busy = stateComp?.state != IDLE
    val hasActions = actor?.hasActions() ?: true

    if (keycode == Keys.SPACE) {
      player[Components.Attack]?.active = true
      logger.info("ATTACK event")
    }

    if (busy && hasActions) {
      return false
    }

    defaultKeymap[keycode]?.let { dir ->
      logger.info("direction key pressed: ${defaultKeymap[keycode]}")
      movementComp?.apply {
        lastMoved = TimeUtils.millis()
        direction = dir
      }
      stateComp?.state = MOVING
    } ?: return false

    return true
  }

  override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
    logger.info("event $event - keycode $keycode")
    event ?: return false
    val dir = defaultKeymap[keycode] ?: return false

    val movementComp = player.movComp()
    // If this isn't the direction we're moving in or if it's not a direction,
    // don't change any state. This probably needs to be tweaked to handle
    // dueling keys sensibly, checking some key inputs (get it) directly.
    if (dir != movementComp?.direction || !dir.isCardinal) return false

    player.stateComp()?.state = IDLE
    movementComp.direction = NONE

    return true
  }

}