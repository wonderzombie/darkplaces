package com.mygdx.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.mygdx.game.constants.Assets.Descriptors
import com.mygdx.game.constants.Assets.Font
import ktx.app.KtxGame
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

class TheGame : KtxGame<DarkPlaces>() {
  private val logger: Logger = Logger("thegame", INFO)
  internal lateinit var assetManager: AssetManager
  internal lateinit var batch: SpriteBatch
  internal lateinit var pcSrFont: BitmapFont

  private val camera: OrthographicCamera = OrthographicCamera()
  internal val engine = PooledEngine()

  override fun create() {
    assetManager = AssetManager()
    batch = SpriteBatch()

    camera.setToOrtho(false, 800f, 480f)

    initEngine(engine)
    initAssets()

    // maybe someday we'll have more than one screen
    addScreen(DarkPlaces(this))
    setScreen(DarkPlaces::class.java)
  }

  private fun initAssets() {
    logger.info("initAssets")
    assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
    assetManager.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))

    assetManager.load(Descriptors.MAP)
    assetManager.load(Descriptors.ACTOR_SHEET)
    assetManager.loadFreeTypeFont(Font.PC_SR)

    pcSrFont = assetManager.finishLoadingAsset(Font.PC_SR)
  }

  private fun initEngine(engine: PooledEngine) {
    logger.info("initEngine")
    engine.addSystem(ActorSystem())
    engine.addSystem(RenderSystem(batch))
    engine.addSystem(MovementSystem())
    logger.info("initialized ${engine.systems.size()} systems")
  }
}