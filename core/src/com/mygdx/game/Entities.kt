package com.mygdx.game

import com.badlogic.ashley.core.Entity
import ktx.ashley.get

internal fun Entity.player(): PlayerComponent? {
  return this[Components.Player]
}