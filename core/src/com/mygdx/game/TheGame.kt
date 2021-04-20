package com.mygdx.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.constants.Assets
import ktx.app.KtxGame


class TheGame : KtxGame<MainScreen>() {
  internal var font: BitmapFont = BitmapFont()
  internal var batch: SpriteBatch = SpriteBatch()
  internal var camera: OrthographicCamera = OrthographicCamera()

  internal var engine = PooledEngine()
  internal var assetManager: AssetManager = AssetManager()

  override fun create() {
    camera.setToOrtho(false, 800f, 480f)
    batch = SpriteBatch()
    font = BitmapFont()

    initEngine()
    initAssets()

    // maybe someday we'll have more than one screen
    addScreen(MainScreen(this))
    setScreen(MainScreen::class.java)
  }

  private fun initAssets() {
    assetManager.load(Assets.Descriptors.NPC_SHEET)
  }

  private fun initEngine() {
  }
}