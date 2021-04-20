package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mygdx.game.constants.AppConstants
import ktx.app.KtxScreen

class MainScreen(private val game: TheGame) : KtxScreen {
  private val orthoCamera: OrthographicCamera = OrthographicCamera()
  private val viewport: ExtendViewport = ExtendViewport(
    AppConstants.DEFAULT_VIEW_WIDTH / 2,
    AppConstants.DEFAULT_VIEW_HEIGHT / 2,
    orthoCamera
  )
  private val stage: Stage = Stage(viewport)

  override fun show() {
    orthoCamera.apply {
      setToOrtho(false, viewportWidth, viewportHeight)
      update()
    }
  }

  override fun dispose() {
    game.engine.clearPools()
    stage.dispose()
  }

  override fun render(delta: Float) {
    ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1f)
    game.engine.update(delta)
  }

  override fun resize(width: Int, height: Int) {
  }

  override fun hide() {
  }

}