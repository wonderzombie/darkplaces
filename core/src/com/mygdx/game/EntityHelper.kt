package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.MovementSystem.Direction
import com.mygdx.game.StateComponent.State
import com.mygdx.game.TypeComponent.Type
import ktx.ashley.get

internal fun Entity.actorComp(): ActorComponent? = this[Components.Actor]
internal fun Entity.actor(): DungeonActor? = this[Components.Actor]?.actor

internal fun Entity.collComp(): CollisionComponent? = this[Components.Collision]
internal fun Entity.boundingRect(): Rectangle? = this[Components.Collision]?.boundingRect

internal fun Entity.movComp(): MovementComponent? = this[Components.Movement]
internal fun Entity.direction(): Direction? = this[Components.Movement]?.direction

internal fun Entity.stateComp(): StateComponent? = this[Components.State]
internal fun Entity.state(): State? = this[Components.State]?.state

internal fun Entity.typeComp(): TypeComponent? = this[Components.Type]
internal fun Entity.type(): Type? = this[Components.Type]?.type