package com.mygdx.game.constants

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Assets {
  class Descriptors {
    companion object {
      val NPC_SHEET: AssetDescriptor<TextureAtlas> =
        AssetDescriptor(Names.NPC_SHEET, TextureAtlas::class.java)

      val SLIME_SHEET: AssetDescriptor<TextureAtlas> =
        AssetDescriptor(Names.SLIME_SHEET_NAME, TextureAtlas::class.java)
    }
  }

  class Names {
    companion object {
      const val SLIME_SHEET_NAME: String = "Slime.txt"
      const val ELITE_KNIGHT_IDLE: String = "Elite_Knight_Idle"
      const val NPC_SHEET = "npc_sheet.txt"
    }
  }
}