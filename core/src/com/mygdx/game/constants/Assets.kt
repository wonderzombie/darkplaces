package com.mygdx.game.constants

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Assets {
  class Descriptors {
    companion object {
      val NPC_SHEET: AssetDescriptor<TextureAtlas> =
        AssetDescriptor(Names.NPC_SHEET, TextureAtlas::class.java)
    }
  }

  class Names {
    companion object {
      const val NPC_SHEET = "npc_sheet.txt"
      const val TOWNSFOLK_IDLE_M = "Townsfolk_Idle_M"
    }
  }
}