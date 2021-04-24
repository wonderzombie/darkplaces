package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf

class CombatSystem : IteratingSystem(allOf(CombatComponent::class).get()) {
  override fun processEntity(entity: Entity?, deltaTime: Float) {
    TODO("Not yet implemented")
  }
}

class CombatComponent : Component {

}
