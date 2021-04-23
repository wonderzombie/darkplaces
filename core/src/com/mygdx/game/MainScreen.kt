package com.mygdx.game

import com.badlogic.gdx.Gdx
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
import com.mygdx.game.constants.Assets.Names
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
      OrthogonalTiledMapRenderer(tiledMap, Assets.Constants.unitScale, game.batch).apply {
        setView(
          orthoCamera
        )
      }

    orthoCamera.apply {
      setToOrtho(false, viewportWidth, viewportHeight)
      update()
    }

    Gdx.input.inputProcessor = stage
    stage.isDebugAll = true

    initAnimations()
    initPlayer(stage, 16f * 2, 16f * 2)
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

    with(stage) {
      act(delta)
      draw()
    }
  }

  override fun resize(width: Int, height: Int) {
  }

  override fun hide() {
  }

  private fun initAnimations() {
    val frames = actorAtlas.findRegions(Names.SLIME_IDLE_R)
    slimeAnim = Animation(0.9f, frames, LOOP)
  }

  private fun Actor.setBounds(textureRegion: AtlasRegion?): Actor {
    textureRegion?.also {
      setBounds(
        it.regionX.toFloat(),
        it.regionY.toFloat(),
        it.regionWidth.toFloat(),
        it.regionHeight.toFloat()
      )
    }
    return this
  }

  private fun initPlayer(stage: Stage, initX: Float, initY: Float) {
    val playerAnim = Animation(0.3f, actorAtlas.findRegions(Assets.Names.HERO_F_IDLE_R), LOOP)
    val newActor = DungeonActor()
    game.engine.add {
      entity {
        with<PlayerComponent> {}

        with<ActorComponent> {
          actor = newActor
          with(actor) {
            setBounds(playerAnim.keyFrames.first())
            setPosition(initX, initY)
            addListener(StageInputListener(this@entity))
          }
          stage.addActor(newActor)
          stage.keyboardFocus = newActor
        }

        with<AnimationComponent> {
          animation = playerAnim
        }

        with<TypeComponent> { type = PLAYER }
        with<StateComponent> { state = IDLE }

        with<MovementComponent> {
          x = newActor.width
          y = newActor.width
        }
      }
    }
  }

  private fun initEntities() {
    logger.info("initEntities")
    val firstFrame = slimeAnim.keyFrames.first()
    game.engine.add {
      // SLIME
      newSlime(16f * 3, 16f * 3, firstFrame)
      newSlime(16f * 10, 16f * 8, firstFrame)
    }.also { logger.info("entities: ${game.engine.entities}") }
  }

  private fun newSlime(stageX: Float, stageY: Float, firstFrame: AtlasRegion) = game.engine.entity {
    val newActor = DungeonActor()
    with<AnimationComponent> {
      animation = slimeAnim
    }

    with<ActorComponent> {
      actor = newActor
      with(firstFrame) {
        actor.setBounds(
          regionX.toFloat(),
          regionY.toFloat(),
          regionWidth.toFloat(),
          regionHeight.toFloat()
        )
      }
      stage.addActor(actor)
    }

    with<StateComponent> { state = IDLE }
    with<TypeComponent> { type = MONSTER }
  }
}
