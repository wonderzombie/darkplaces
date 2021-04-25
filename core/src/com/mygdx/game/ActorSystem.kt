package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.Components.Companion.Actor
import com.mygdx.game.Components.Companion.State
import com.mygdx.game.Components.Companion.Type
import com.mygdx.game.StateComponent.State.DEAD
import com.mygdx.game.StateComponent.State.HIT
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.TypeComponent.Type.PLAYER
import com.mygdx.game.constants.Assets.ActorFX
import ktx.ashley.allOf
import ktx.math.vec2


internal class ActorComponent : Component {
  lateinit var actor: DungeonActor
}

class DungeonActor : Actor() {
  private var id: String = "Actor|${TimeUtils.millis()}"
  var stateTime: Float = 0f

  fun upateRect(rect: Rectangle): Rectangle =
    rect.set(x, y, width, height)

  val pos: Vector2 = vec2()
    get() = field.set(this.x, this.y)


  fun Actor.setBounds(textureRegion: AtlasRegion?): Actor {
    textureRegion?.also {
      setBounds(
        it.regionX.toFloat(),
        it.regionY.toFloat(),
        it.regionWidth.toFloat(),
        it.regionHeight.toFloat()
      )
    }
    return this
  }
}

class ActorSystem : IteratingSystem(
  allOf(ActorComponent::class, StateComponent::class, TypeComponent::class).get(),
  EnginePriority.Actor
) {
  companion object {
    private val logger = Logger("act", Logger.INFO)
  }

  override fun addedToEngine(engine: Engine?) {
    engine?.addEntityListener(ActorListener())
    super.addedToEngine(engine)
  }

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    entity ?: return
    val actorComp = Actor.get(entity) ?: return
    actorComp.actor.stateTime += deltaTime

    val stateComp = State.get(entity)
    val type = Type.get(entity)

    if (TimeUtils.timeSinceMillis(stateComp.stateTime) == 0L) {
      when (stateComp.state) {
        HIT -> applyHitEffect(actorComp.actor, type)
        DEAD -> applyDeadEffect(actorComp.actor, type)
      }
    }
  }

  private fun applyDeadEffect(actor: DungeonActor, typeComp: TypeComponent) {
    val effect = when (typeComp.type) {
      MONSTER -> ActorFX.DEAD_ACTION
      else -> null
    }
    effect?.also { actor.addAction(it) }
  }

  private fun applyHitEffect(actor: DungeonActor, typeComp: TypeComponent) {
    val effect = when (typeComp.type) {
      MONSTER -> ActorFX.HIT_RED_ACTION
      PLAYER -> ActorFX.HIT_BLINK_ACTION
      else -> null
    }
    effect?.also { actor.addAction(it) }
  }

  inner class ActorListener : EntityListener {
    override fun entityAdded(entity: Entity?) {
      entity ?: return
    }

    override fun entityRemoved(entity: Entity?) {
      entity ?: return
    }
  }
}


