package com.mygdx.game.constants

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import ktx.actors.then

class Assets {
  class Constants {
    companion object {
      const val UNIT_SCALE = 1f
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
      val SLIME_SHEET: AssetDescriptor<TextureAtlas> =
        AssetDescriptor(Names.SLIME_SHEET_NAME, TextureAtlas::class.java)
    }
  }

  class MapProperties {
    companion object {
      const val TYPE = "type"
    }
  }

  class Names {
    companion object {
      const val MAP_NAME: String = "darkplaces_map.tmx"

      const val ACTOR_SHEET_NAME: String = "dungeon_sprites.txt"
      const val HERO_F_IDLE_R: String = "fHero_/idle_/rIdle"
      const val HERO_F_WALKRUN_R: String = "fHero_/walkRun_/rWalkRun"

      const val SLIME_IDLE_R: String = "slime_/idle_/rIdle"

      const val SLIME_SHEET_NAME: String = "Slime.txt"
    }
  }
}