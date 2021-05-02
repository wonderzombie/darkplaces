package com.mygdx.game.constants

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import ktx.actors.then

class Assets {
  class Constants {
    companion object {
      const val UNIT_SCALE = 1f

      val ZeroRect: Rectangle = Rectangle(0f, 0f, 0f, 0f)
    }
  }

  class ActorFX {
    companion object {
      val HIT_BLINK_ACTION: RepeatAction
        get() = Actions.repeat(
          5,
          Actions.sequence(Actions.fadeOut(0.07f), Actions.fadeIn(0.07f))
        )

      val HIT_RED_ACTION: Action
        get() = Actions.color(Color.FIREBRICK, 0.1f, Interpolation.fastSlow)
          .then(Actions.color(Color.WHITE, 0.1f, Interpolation.fastSlow))

      val DEAD_ACTION: Action
        get() = Actions.parallel(
          Actions.color(Color.DARK_GRAY, 1f),
          Actions.rotateBy(90f, 0.5f)
        )
    }
  }

  class Descriptors {
    companion object {
      val ACTOR_SHEET = AssetDescriptor(Names.ACTOR_SHEET_NAME, TextureAtlas::class.java)
      val MAP: AssetDescriptor<TiledMap> = AssetDescriptor(Names.MAP_NAME, TiledMap::class.java)
    }
  }

  class MapProperties {
    class MapObj {
      companion object {
        const val TYPE = "type"
      }
    }
  }

  class Monsters {
    companion object {
      const val SLIME: String = "slime"
    }
  }

  class Names {
    companion object {
      const val MAP_NAME: String = "darkplaces_map.tmx"

      const val ACTOR_SHEET_NAME: String = "dungeon_sprites.txt"

      const val HERO_F_IDLE_L: String = "fHero_/idle_/lIdle"
      const val HERO_F_IDLE_R: String = "fHero_/idle_/rIdle"

      const val HERO_F_WALKRUN_L: String = "fHero_/walkRun_/lWalkRun"
      const val HERO_F_WALKRUN_R: String = "fHero_/walkRun_/rWalkRun"

      const val SLIME_IDLE_R: String = "slime_/idle_/rIdle"
      const val SLIME_IDLE_L: String = "slime_/idle_/lIdle"

      const val WEAPON_SWORD: String = "sword_"
      const val HAMMER_SWORD: String = "hammer_"
      const val STAFF_SWORD: String = "staff_"
      const val AXE_SWORD: String = "axe_"
    }
  }

  class Font {
    companion object {
      const val PC_SR: String = "pcsenior.ttf"
    }
  }
}