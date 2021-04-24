package com.mygdx.game

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.PlayerInputListener.Direction.DOWN
import com.mygdx.game.PlayerInputListener.Direction.LEFT
import com.mygdx.game.PlayerInputListener.Direction.NONE
import com.mygdx.game.PlayerInputListener.Direction.RIGHT
import com.mygdx.game.PlayerInputListener.Direction.UP
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
    logger.info("event $event - keycode $keycode")
    if (keycode !in defaultKeymap) return false

    val actor = player[Components.Actor]?.actor
    val movementComp = player[Components.Movement]
    val stateComp = player[Components.State]

    val busy = stateComp?.state != IDLE
    val hasActions = actor?.hasActions() ?: true

    if (busy && hasActions) {
      return false
    }

    defaultKeymap[keycode]?.let { dir ->
      logger.info("direction key pressed: ${defaultKeymap[keycode]}")
      movementComp?.apply {
        lastMoved = TimeUtils.millis()
        direction = dir
        lastDirection = defaultKeymap[keycode] ?: NONE
      }
      stateComp?.state = MOVING
    } ?: return false

    return true
  }

  override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
    logger.info("event $event - keycode $keycode")
    event ?: return false
    val dir = defaultKeymap[keycode] ?: return false

    val movementComp = player[Components.Movement]
    if (defaultKeymap[keycode] != movementComp?.direction) return false

    if (!dir.isCardinal) return false

    val stateComp = player[Components.State]
    stateComp?.state = IDLE
    movementComp?.direction = NONE

    return true
  }

  enum class Direction(internal val x: Float, internal val y: Float) {
    NONE(0f, 0f),
    UP(0f, 1f),
    RIGHT(1f, 0f),
    DOWN(0f, -1f),
    LEFT(-1f, 0f);

    val isCardinal: Boolean
      get() = this != NONE
  }
}