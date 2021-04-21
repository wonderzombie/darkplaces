package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.TypeComponent.Type.PLAYER
import com.mygdx.game.constants.AppConstants
import com.mygdx.game.constants.Assets
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with

class MainScreen(private val game: TheGame) : KtxScreen {
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

//    npcSheet = game.assetManager.get(Assets.Descriptors.NPC_SHEET)

    orthoCamera.apply {
      setToOrtho(false, viewportWidth, viewportHeight)
      update()
    }

    initAnim()
//    initPlayer()
    initEntities()
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

  private fun initAnim() {
    val frames = game.assetManager.get(Assets.Descriptors.SLIME_SHEET)?.findRegions("Slime")
    slimeAnim = Animation(0.3f, frames).also { it.playMode = LOOP }
  }

  private fun initPlayer() {
    game.engine.add {
      entity {
        with<PlayerComponent> {}

        with<ActorComponent> {}.also { stage.addActor(it.actor) }
        with<AnimationComponent> {
          animation = Animation(0.3f, npcSheet.findRegions(Assets.Names.ELITE_KNIGHT_IDLE), LOOP)
        }
        with<TypeComponent> { type = PLAYER }
        with<StateComponent> {}
      }
    }
  }

  private fun initEntities() {
    val firstFrame = slimeAnim.keyFrames.first()
    game.engine.add {
      (0..2).onEach {
        entity {
          with<AnimationComponent> {
            animation = slimeAnim
          }
          with<ActorComponent> {
            actor.setPosition(100f, 100f)
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