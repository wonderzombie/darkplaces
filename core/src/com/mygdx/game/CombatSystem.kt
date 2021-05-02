package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.Components.Companion.Attack
import com.mygdx.game.Components.Companion.Collision
import com.mygdx.game.Components.Companion.Combat
import com.mygdx.game.Components.Companion.State
import com.mygdx.game.Components.Companion.Type
import com.mygdx.game.StateComponent.State.DEAD
import com.mygdx.game.StateComponent.State.HIT
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.constants.Assets.MapProperties.MapObj.Companion.TYPE
import ktx.ashley.allOf

val family: Family =
  allOf(
    AttackComponent::class,
    CombatComponent::class,
    CollisionComponent::class,
    TypeComponent::class
  ).get()

val logger: Logger = Logger("com", INFO)

val hazards: Map<String, Int> = mapOf(
  "spikes" to 3
)

class CombatSystem(playerGroup: Group) : IteratingSystem(family, EnginePriority.Combat) {
  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return

    val stateComp = State.get(entity)
    if (stateComp.state == DEAD) return

    val comComp = Combat.get(entity)
    val collComp = Collision.get(entity)
    val typeComp = Type.get(entity)
    val attackComp = Attack.get(entity)

    applyHazardEffects(
      entity.actor()?.name ?: "(none)",
      collComp,
      comComp
    ).also { if (it) stateComp.state = HIT }
    applyCombatEffects(collComp, comComp, attackComp)

    if (checkDead(comComp)) {
      stateComp.state = when (typeComp.type) {
        MONSTER -> DEAD.also { logger.info("dead monster") }
        else -> stateComp.state
      }
    }
  }

  private fun applyCombatEffects(
    collComp: CollisionComponent,
    comComp: CombatComponent,
    attackComp: AttackComponent,
  ) {

  }

  private fun applyHazardEffects(
    name: String,
    collComp: CollisionComponent,
    com: CombatComponent
  ): Boolean {
    val rectMapObj = collComp.lastMapObjColl as RectangleMapObject?
    rectMapObj ?: return false

    val hazardDamage = getHazardDamage(rectMapObj)
    if (hazardDamage != 0 && TimeUtils.timeSinceMillis(com.lastHitTime) >= 1000L) {
      logger.info("damaged entity $name @ ${com.health} hp for $hazardDamage")
      applyDamage(com, hazardDamage)
    }


    if (com.lastHitBy != null && TimeUtils.timeSinceMillis(com.lastHitTime) <= com.hitCooldown) {
    }

    return true
  }

  private fun getHazardDamage(rectMapObj: RectangleMapObject): Int {
    return rectMapObj.properties.get(TYPE, "", String::class.java)?.let {
      hazards[it.toLowerCase()]
    } ?: 0
  }

  private fun applyDamage(comComp: CombatComponent, damage: Int) =
    comComp.apply {
      health -= damage
      lastHitTime = TimeUtils.millis()
    }

  private fun checkDead(comComp: CombatComponent): Boolean {
    if (!comComp.mortal) return false
    return comComp.health <= 0
  }

}

data class CombatComponent(
  var health: Int = 1,
  var lastHitBy: Entity? = null,
  var lastHitTime: Long = 0,
  var mortal: Boolean = false,
  var hitCooldown: Long = 3000L,
) : Component
