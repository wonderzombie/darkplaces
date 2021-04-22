package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mygdx.game.StateComponent.State.IDLE
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
  private val logger: Logger = Logger("main")

  override fun show() {
    game.assetManager.finishLoading()

    actorAtlas = game.assetManager.get(Descriptors.ACTOR_SHEET)

    tiledMap = game.assetManager.get(Descriptors.MAP)
    tiledMapRenderer =
      OrthogonalTiledMapRenderer(tiledMap, 1f, game.batch).apply { setView(orthoCamera) }

    orthoCamera.apply {
      setToOrtho(false, viewportWidth, viewportHeight)
      update()
    }

    initAnim()
    initPlayer()
    initEntities()
  }

  override fun dispose() {
    stage.dispose()
    tiledMap.dispose()
    tiledMapRenderer.dispose()
    actorAtlas.dispose()
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
    slimeAnim = Animation(0.9f, frames, LOOP)
  }

  private fun Actor.setBounds(textureRegion: AtlasRegion?): Actor {
    textureRegion?.also {
      setBounds(
        textureRegion.regionX.toFloat(),
        textureRegion.regionY.toFloat(),
        textureRegion.regionWidth.toFloat(),
        textureRegion.regionHeight.toFloat()
      )
    }
    return this
  }

  private fun initPlayer() {
    val playerAnim = Animation(0.3f, actorAtlas.findRegions(Assets.Names.HERO_F_IDLE_R), LOOP)
    game.engine.add {
      entity {
        with<PlayerComponent> {}

        with<ActorComponent> {
          actor.setPosition(16f * 5, 16f * 5)
          actor.setBounds(playerAnim.keyFrames.first())
        }
        with<AnimationComponent> {
          animation = playerAnim
        }
        with<TypeComponent> { type = PLAYER }
        with<StateComponent> { state = IDLE }
      }
    }
  }

  private fun initEntities() {
    logger.info("initEntities")
    val firstFrame = slimeAnim.keyFrames.first()
    game.engine.add {
      // SLIME
      entity {
        with<AnimationComponent> {
          animation = slimeAnim
        }
        with<ActorComponent> {
          actor.setPosition(16f * 3, 16f * 3)
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
    }.also { logger.info("entities: ${game.engine.entities}") }
  }

}
