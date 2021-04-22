package com.mygdx.game.constants

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap

class Assets {
  class Descriptors {
    companion object {
      val ACTOR_SHEET = AssetDescriptor(Names.ACTOR_SHEET_NAME, TextureAtlas::class.java)

      //      val SHADOWS: AssetDescriptor<
      val MAP: AssetDescriptor<TiledMap> = AssetDescriptor(Names.MAP_NAME, TiledMap::class.java)

      val SLIME_SHEET: AssetDescriptor<TextureAtlas> =
        AssetDescriptor(Names.SLIME_SHEET_NAME, TextureAtlas::class.java)

//      val NPC_SHEET: AssetDescriptor<TextureAtlas> =
//        AssetDescriptor(Names.NPC_SHEET, TextureAtlas::class.java)
    }
  }

  class Names {
    companion object {
      const val MAP_NAME: String = "darkplaces_map.tmx"

      const val ACTOR_SHEET_NAME: String = "dungeon_sprites.txt"
      const val HERO_F_IDLE_R: String = "fHero_/idle_/rIdle"

      const val SLIME_SHEET_NAME: String = "Slime.txt"
    }
  }
}