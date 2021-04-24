package com.mygdx.game

import com.badlogic.ashley.core.Entity
import ktx.ashley.get

internal fun Entity.playerComp(): PlayerComponent? {
  return this[Components.Player]
}

internal fun Entity.movComp(): MovementComponent? {
  return this[Components.Movement]
}

internal fun Entity.actorComp(): ActorComponent? {
  return this[Components.Actor]
}

internal fun Entity.collComp(): CollisionComponent? {
  return this[Components.Collision]
}

internal fun Entity.stateComp(): StateComponent? {
  return this[Components.State]
}