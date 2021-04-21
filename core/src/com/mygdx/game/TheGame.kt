package com.mygdx.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mygdx.game.constants.Assets.Descriptors
import ktx.app.KtxGame

class TheGame : KtxGame<MainScreen>() {
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
    assetManager.load(Descriptors.SLIME_SHEET)

    assetManager.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
    assetManager.load(Descriptors.MAP)
  }

  private fun initEngine(engine: PooledEngine) {
    engine.addSystem(PlayerSystem())
    engine.addSystem(ActorSystem())
    engine.addSystem(RenderSystem(batch))
  }

  class PlayerSystem : IteratingSystem(Family.one(PlayerComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
      entity ?: return
    }
  }

  class ActorSystem : IteratingSystem(Family.all(ActorComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
      entity ?: return
    }
  }

  class RenderSystem(private val batch: SpriteBatch, family: Family = RenderSystem.family) :
    IteratingSystem(family) {
    companion object {
      val family: Family =
        Family.all(
          ActorComponent::class.java,
          AnimationComponent::class.java
        ).get()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
      entity ?: return

      val animComp = Components.animMapper.get(entity)
      val actorComp = Components.actorMapper.get(entity)

      val anim = with(animComp) {
        stateTime += deltaTime
        if (animation.keyFrames.isNotEmpty()) animation else texAnimation
      }

      batch.begin()
      with(actorComp.actor) {
        batch.draw(
          anim.getKeyFrame(animComp.stateTime),
          x, y, originX, originY, width, height, scaleX, scaleY, rotation
        )
      }
      batch.end()
    }

  }
}