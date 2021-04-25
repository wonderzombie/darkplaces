package com.mygdx.game

import com.badlogic.ashley.core.Entity
import ktx.ashley.get

internal fun Entity.actorComp(): ActorComponent? = this[Components.Actor]
internal fun Entity.collComp(): CollisionComponent? = this[Components.Collision]
internal fun Entity.movComp(): MovementComponent? = this[Components.Movement]
internal fun Entity.stateComp(): StateComponent? = this[Components.State]
internal fun Entity.typeComp(): TypeComponent? = this[Components.Type]