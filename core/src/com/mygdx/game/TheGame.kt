package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Logger
import com.mygdx.game.constants.Assets.Descriptors
import ktx.app.KtxGame
import ktx.ashley.allOf

class TheGame : KtxGame<MainScreen>() {
  private val logger: Logger = Logger("thegame")
  internal lateinit var font: BitmapFont
  internal lateinit var assetManager: AssetManager
  internal lateinit var batch: SpriteBatch

  private val camera: OrthographicCamera = OrthographicCamera()
  internal val engine = PooledEngine()

  override fun create() {
    font = BitmapFont()
    assetManager = AssetManager()
    batch = SpriteBatch()

    camera.setToOrtho(false, 800f, 480f)

    initEngine(engine)
    initAssets()

    // maybe someday we'll have more than one screen
    addScreen(MainScreen(this))
    setScreen(MainScreen::class.java)
  }


  private fun initAssets() {
    logger.info("initAssets")

    assetManager.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))

    assetManager.load(Descriptors.MAP)
    assetManager.load(Descriptors.ACTOR_SHEET)
    assetManager.load(Descriptors.SLIME_SHEET)
  }

  private fun initEngine(engine: PooledEngine) {
    logger.info("initEngine")
    engine.addSystem(PlayerSystem())
    engine.addSystem(ActorSystem())
    engine.addSystem(RenderSystem(batch))
    logger.info("initialized ${engine.systems.size()} systems")
  }

  class PlayerSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
      entity ?: return
    }
  }

  class InputSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
      entity ?: return
    }
  }


}