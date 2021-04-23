package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.mygdx.game.StageInputListener.Direction.NONE
import com.mygdx.game.StateComponent.State.IDLE
import ktx.actors.then
import ktx.ashley.EngineEntity
import ktx.ashley.allOf
import ktx.ashley.get

class InputSystem : IteratingSystem(allOf(PlayerComponent::class).get(), 0) {
  override fun processEntity(entity: Entity?, deltaTime: Float) {
  }
}

class StageInputListener(controlledEntity: EngineEntity) : InputListener() {
  private val logger: Logger = Logger("input", INFO)
  private val player = controlledEntity.entity

  override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
    logger.info("event $event - keycode $keycode")
    if (keycode !in Direction.defaultKeymap) return false

    event ?: return false.also { logger.error("empty entity detected") }

    val actor = player[Components.Actor]?.actor ?: return false
    val movement = player[Components.Movement] ?: return false
    val stateComponent = player[Components.State] ?: return false

    val canMove = stateComponent.state == IDLE
    val tooBusy = actor.hasActions()

    if (!canMove && tooBusy) {
      return false
    }

    Direction.defaultKeymap[keycode]?.let { dir ->
      logger.info("direction key pressed: ${Direction.defaultKeymap[keycode]}")
      actor.addAction(
        Actions.moveBy(
          dir.x * actor.width, dir.y * actor.width, movement.duration, movement.interp
        ).then(Actions.run { logger.info("now idle"); stateComponent.state = IDLE })
      )
      movement.lastDirection = Direction.defaultKeymap[keycode] ?: NONE
    } ?: return false

    return true
  }

  enum class Direction(internal val x: Float, internal val y: Float) {
    NONE(0f, 0f),
    UP(0f, 1f),
    RIGHT(1f, 0f),
    DOWN(0f, -1f),
    LEFT(-1f, 0f);

//    val vec: Vector2 by lazy { vec2(x, y) }

    companion object {
      val defaultKeymap = mapOf(
        Keys.UP to UP,
        Keys.RIGHT to RIGHT,
        Keys.DOWN to DOWN,
        Keys.LEFT to LEFT
      )
    }
  }
}