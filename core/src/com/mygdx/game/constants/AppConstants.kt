package com.mygdx.game.constants

import com.badlogic.gdx.Gdx

class AppConstants {
  companion object {
    const val DEFAULT_VIEW_WIDTH = 640f
    const val DEFAULT_VIEW_HEIGHT = 480f

    const val DEFAULT_WIN_WIDTH = 1024
    const val DEFAULT_WIN_HEIGHT = 768
  }

  class Debug {
    companion object {
      val mouseDebugStr: String
        get() = "Mouse: ${Gdx.input.x},${Gdx.input.y}"
    }
  }
}