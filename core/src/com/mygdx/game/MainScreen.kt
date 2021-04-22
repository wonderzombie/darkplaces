package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.TypeComponent.Type.PLAYER
import com.mygdx.game.constants.AppConstants
import com.mygdx.game.constants.Assets
import com.mygdx.game.constants.Assets.Descriptors
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with

class MainScreen(private val game: TheGame) : KtxScreen {
  private lateinit var actorAtlas: TextureAtlas
  private lateinit var tiledMapRenderer: OrthogonalTiledMapRenderer
  private lateinit var tiledMap: TiledMap
  private lateinit var slimeAnim: Animation<AtlasRegion>
  private lateinit var npcSheet: TextureAtlas

  private val orthoCamera: OrthographicCamera = OrthographicCamera()
  private val viewport: ExtendViewport = ExtendViewport(
    AppConstants.DEFAULT_VIEW_WIDTH / 2,
    AppConstants.DEFAULT_VIEW_HEIGHT / 2,
    orthoCamera
  )
  private val stage: Stage = Stage(viewport)

  override fun show() {
    game.assetManager.finishLoading()

    actorAtlas = game.assetManager.get(Descriptors.ACTOR_SHEET)

    tiledMap = game.assetManager.get(Descriptors.MAP)
    tiledMapRenderer =
      OrthogonalTiledMapRenderer(tiledMap, game.batch).apply { setView(orthoCamera) }

    orthoCamera.apply {
      setToOrtho(false, viewportWidth, viewportHeight)
      update()
    }

    initAnim()
    initPlayer()
    initEntities()
  }

  override fun dispose() {
    game.engine.clearPools()
    stage.dispose()
    tiledMap.dispose()
    tiledMapRenderer.dispose()
    npcSheet.dispose()
  }

  override fun render(delta: Float) {
    ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1f)
    tiledMapRenderer.render()

    game.engine.update(delta)
  }

  override fun resize(width: Int, height: Int) {
  }

  override fun hide() {
  }

  private fun initAnim() {
    val frames =
      game.assetManager.get(Assets.Descriptors.SLIME_SHEET).findRegions("Slime")
    slimeAnim = Animation(0.9f, frames).also { it.playMode = LOOP }
  }

  private fun initPlayer() {
    game.engine.add {
      entity {
        with<PlayerComponent> {}

        with<ActorComponent> {}.also { stage.addActor(it.actor) }
        with<AnimationComponent> {
          animation = Animation(0.3f, actorAtlas.findRegions(Assets.Names.HERO_F_IDLE_R), LOOP)
        }
        with<TypeComponent> { type = PLAYER }
        with<StateComponent> {}
      }
    }
  }

  private fun initEntities() {
    val firstFrame = slimeAnim.keyFrames.first()
    game.engine.add {
      (0..0).onEach {
        entity {
          with<AnimationComponent> {
            animation = slimeAnim
          }
          with<ActorComponent> {
            actor.setPosition(10f * it, 10f * it)
            with(firstFrame) {
              actor.setBounds(
                regionX.toFloat(),
                regionY.toFloat(),
                regionWidth.toFloat(),
                regionHeight.toFloat()
              )
            }
          }
          with<StateComponent> {}
          with<TypeComponent> { type = MONSTER }
        }
      }
    }

  }

}