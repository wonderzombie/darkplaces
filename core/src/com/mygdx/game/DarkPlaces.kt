package com.mygdx.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.mygdx.game.MovementSystem.Direction
import com.mygdx.game.MovementSystem.Direction.LEFT
import com.mygdx.game.MovementSystem.Direction.RIGHT
import com.mygdx.game.StateComponent.State.IDLE
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.TypeComponent.Type.PLAYER
import com.mygdx.game.constants.AppConstants
import com.mygdx.game.constants.Assets
import com.mygdx.game.constants.Assets.Descriptors
import com.mygdx.game.constants.Assets.MapProperties.MapObj.Companion.TYPE
import com.mygdx.game.constants.Assets.Monsters
import com.mygdx.game.constants.Assets.Names
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y

class DarkPlaces(private val game: TheGame) : KtxScreen {
  private lateinit var playerIdleAnimL: Animation<AtlasRegion>
  private lateinit var playerIdleAnimR: Animation<AtlasRegion>
  private lateinit var playerMoveAnimL: Animation<AtlasRegion>
  private lateinit var playerMoveAnimR: Animation<AtlasRegion>

  private lateinit var actorAtlas: TextureAtlas
  private lateinit var tiledMapRenderer: OrthogonalTiledMapRenderer
  private lateinit var tiledMap: TiledMap
  private lateinit var slimeAnim: Animation<AtlasRegion>

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
      OrthogonalTiledMapRenderer(tiledMap, Assets.Constants.UNIT_SCALE, game.batch).apply {
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

    val playerSpawn = tiledMap.layer("Transitions").objects.find {
      it?.properties?.get(
        TYPE,
        String::class.java
      ) == "Spawn" && it.name == "Player"
    }?.let { vec2(it.x, it.y) } ?: Vector2.Zero

    val slimeSpawns =
      tiledMap.layer("Transitions").objects.filter { it.name.toLowerCase() == Monsters.SLIME }

    initEngine(game.engine)
    initAnimations()
    initPlayer(stage, playerSpawn.x, playerSpawn.y)
    initEntities(slimeSpawns)
  }

  private fun initEngine(engine: PooledEngine) {
    engine.addSystem(CollisionSystem(tiledMap.layer("Walls"), tiledMap.layer("Hazards")))
    engine.addSystem(CombatSystem())
  }

  override fun dispose() {
    stage.dispose()
    tiledMap.dispose()
    tiledMapRenderer.dispose()
    actorAtlas.dispose()
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
    playerIdleAnimL = Animation(0.3f, actorAtlas.findRegions(Names.HERO_F_IDLE_L), LOOP)
    playerIdleAnimR = Animation(0.3f, actorAtlas.findRegions(Names.HERO_F_IDLE_R), LOOP)
    playerMoveAnimR = Animation(0.2f, actorAtlas.findRegions(Names.HERO_F_WALKRUN_R), LOOP)
    playerMoveAnimL = Animation(0.2f, actorAtlas.findRegions(Names.HERO_F_WALKRUN_L), LOOP)

    check(playerMoveAnimL.keyFrames.isNotEmpty())
    check(playerMoveAnimR.keyFrames.isNotEmpty())
    check(playerIdleAnimL.keyFrames.isNotEmpty())
    check(playerIdleAnimR.keyFrames.isNotEmpty())
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

  private fun initPlayer(stage: Stage, initX: Float = 16f * 3, initY: Float = 16f * 3) {
    val newActor = DungeonActor()
    game.engine.add {
      entity {
        with<PlayerComponent> {
          name = "HERO"
        }

        with<ActorComponent> {
          actor = newActor
          with(actor) {
            setBounds(playerIdleAnimL.keyFrames.first())
            setPosition(initX, initY)
            addListener(PlayerInputListener(this@entity))
          }
          stage.addActor(newActor)
          stage.keyboardFocus = newActor
        }

        with<AnimationComponent> {
          idle = mapOf(
            LEFT to playerIdleAnimL,
            RIGHT to playerIdleAnimR
          )

          moving = mapOf(
            LEFT to playerMoveAnimL,
            RIGHT to playerMoveAnimR,
          )
        }

        with<TypeComponent> { type = PLAYER }
        with<StateComponent> { state = IDLE }

        with<MovementComponent> {
          x = 40f
          y = 40f
          interp = Interpolation.fastSlow
        }

        with<CollisionComponent> {
          newActor.upateRect(boundingRect)
        }

        with<CombatComponent> {
          health = 10
        }
      }
    }
  }

  private fun initEntities(slimeSpawns: List<MapObject>) {
    logger.info("initEntities")

    game.engine.add {
      slimeSpawns.onEach { spawn ->
        newSlime(spawn.x, spawn.y, slimeAnim.keyFrames.first())
      }
    }
  }

  private fun newSlime(stageX: Float, stageY: Float, firstFrame: AtlasRegion) = game.engine.entity {
    val newActor = DungeonActor()
    with<AnimationComponent> {
      idle = mapOf(
        LEFT to slimeAnim,
        RIGHT to slimeAnim,
      )
      moving = mapOf(
        LEFT to slimeAnim,
        RIGHT to slimeAnim,
      )
    }

    with<ActorComponent> {
      actor = newActor
      actor.setBounds(firstFrame)
      actor.setPosition(stageX, stageY)
      stage.addActor(actor)
    }

    with<CombatComponent> {
      health = 5
    }

    with<StateComponent> { state = IDLE }
    with<TypeComponent> { type = MONSTER; subtype = "slime" }
  }
}
