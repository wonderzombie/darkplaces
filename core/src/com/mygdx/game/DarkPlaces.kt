package com.mygdx.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Logger
import com.badlogic.gdx.utils.Logger.INFO
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisLabel
import com.mygdx.game.MovementSystem.Direction.LEFT
import com.mygdx.game.MovementSystem.Direction.RIGHT
import com.mygdx.game.StateComponent.State.IDLE
import com.mygdx.game.TypeComponent.Type.MONSTER
import com.mygdx.game.TypeComponent.Type.PLAYER
import com.mygdx.game.constants.AppConstants
import com.mygdx.game.constants.AppConstants.Debug
import com.mygdx.game.constants.Assets
import com.mygdx.game.constants.Assets.Descriptors
import com.mygdx.game.constants.Assets.Font
import com.mygdx.game.constants.Assets.MapProperties.MapObj.Companion.TYPE
import com.mygdx.game.constants.Assets.Monsters
import com.mygdx.game.constants.Assets.Names
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.DisposableContainer
import ktx.math.vec2
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.style.label
import ktx.style.skin
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y

class DarkPlaces(private val game: TheGame) : KtxScreen {
  private val disposableContainer: DisposableContainer = DisposableContainer()

  private val orthoCamera: OrthographicCamera = OrthographicCamera()
  private val viewport: ExtendViewport = ExtendViewport(
    AppConstants.DEFAULT_VIEW_WIDTH / 2,
    AppConstants.DEFAULT_VIEW_HEIGHT / 2,
    orthoCamera
  )
  private val stage: Stage = Stage(viewport)
  private val logger: Logger = Logger("darkplaces", INFO)

  // animations begging for another home
  private lateinit var slimeAnimR: Animation<AtlasRegion>
  private lateinit var slimeAnimL: Animation<AtlasRegion>

  private lateinit var playerIdleAnimL: Animation<AtlasRegion>
  private lateinit var playerIdleAnimR: Animation<AtlasRegion>
  private lateinit var playerMoveAnimL: Animation<AtlasRegion>
  private lateinit var playerMoveAnimR: Animation<AtlasRegion>

  // resources
  private lateinit var actorAtlas: TextureAtlas
  private lateinit var tiledMapRenderer: OrthogonalTiledMapRenderer
  private lateinit var tiledMap: TiledMap

  // scene2d
  private lateinit var screenTable: Table

  override fun show() {
    game.assetManager.finishLoading()

    actorAtlas =
      game.assetManager.get(Descriptors.ACTOR_SHEET).also { disposableContainer.register(it) }

    tiledMap = game.assetManager.get(Descriptors.MAP).also { disposableContainer.register(it) }
    tiledMapRenderer =
      OrthogonalTiledMapRenderer(tiledMap, Assets.Constants.UNIT_SCALE, game.batch).apply {
        setView(
          orthoCamera
        )
      }.also { disposableContainer.register(it) }

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
    check(slimeSpawns.isNotEmpty())

    initEngine(game.engine)
    initAnimations()
    initPlayer(stage, playerSpawn.x, playerSpawn.y)
    initEntities(slimeSpawns)

    initUi()
  }

  private fun getFancyFont(): BitmapFont? {
    val generator = FreeTypeFontGenerator(Gdx.files.internal(Font.PC_SR))
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    return generator.generateFont(parameter).also { generator.dispose() }
  }

  private fun initUi() {
    game.assetManager.finishLoadingAsset<BitmapFont>(Font.PC_SR)
    game.pcSrFont = game.assetManager.get(Font.PC_SR)

    VisUI.load()

    screenTable =
      scene2d {
        visTable {
          setFillParent(true)
          name = "debugTable"
          debug = true
          align(Align.topLeft)
          visLabel(Debug.mouseDebugStr) {
            style = LabelStyle(getFancyFont(), Color.WHITE)
          }
        }
      }
    stage.addActor(screenTable)
  }

  private fun initEngine(engine: PooledEngine) {
    engine.addSystem(CollisionSystem(tiledMap.layer("Walls"), tiledMap.layer("Hazards")))
    engine.addSystem(CombatSystem())
  }

  override fun dispose() {
    disposableContainer.dispose()
    VisUI.dispose()
  }

  override fun render(delta: Float) {
    ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1f)

    val uiActor = screenTable.children.firstOrNull()
    uiActor.apply { if (this is VisLabel) setText(Debug.mouseDebugStr) }

    tiledMapRenderer.render()
    game.engine.update(delta)

    with(stage) {
      act(delta)
      draw()
    }

    game.batch.begin()
    game.pcSrFont.draw(game.batch, "Mouse: ${Gdx.input.x},${Gdx.input.y}", 50f, 10f)
    game.batch.end()
  }

  override fun resize(width: Int, height: Int) {
  }

  override fun hide() {
  }

  private fun initAnimations() {
    slimeAnimR = checkedGetAnim(Names.SLIME_IDLE_R, 0.7f)
    slimeAnimL = checkedGetAnim(Names.SLIME_IDLE_L, 0.7f)

    playerIdleAnimL = checkedGetAnim(Names.HERO_F_IDLE_L, 0.3f)
    playerIdleAnimR = checkedGetAnim(Names.HERO_F_IDLE_R, 0.3f)
    playerMoveAnimR = checkedGetAnim(Names.HERO_F_WALKRUN_R, 0.2f)
    playerMoveAnimL = checkedGetAnim(Names.HERO_F_WALKRUN_L, 0.2f)
  }

  private fun checkedGetAnim(name: String, frameDuration: Float) =
    Animation(
      frameDuration,
      actorAtlas.findRegions(name),
      LOOP
    ).also {
      check(
        it.keyFrames.isNotEmpty(),
        lazyMessage = { "anim: expected $name not to be empty" })
      check(it.keyFrames.first().regionHeight != 0 && it.keyFrames.first().regionWidth != 0)
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

    slimeSpawns.onEach {
      newSlime(it.x, it.y, slimeAnimR.keyFrames.first())
    }

    check(game.engine.entities.any {
      it?.typeComp()?.isMonster == true && it.typeComp()?.subtype?.toLowerCase()
        ?.contentEquals("slime") == true
    })
  }

  private fun newSlime(stageX: Float, stageY: Float, firstFrame: AtlasRegion) =
    game.engine.entity {
      logger.info("new slime @ $stageX,$stageY")
      val newActor = DungeonActor()
      with<AnimationComponent> {
        idle = mapOf(
          LEFT to slimeAnimR,
          RIGHT to slimeAnimR,
        )
        moving = mapOf(
          LEFT to slimeAnimR,
          RIGHT to slimeAnimR,
        )
      }

      with<ActorComponent> {
        this.actor = newActor
        this.actor.setBounds(firstFrame)
        this.actor.setPosition(stageX, stageY)
        stage.addActor(this.actor)
      }

      with<CollisionComponent> {
        this.boundingRect = newActor.upateRect(boundingRect)
      }

      with<CombatComponent> {
        health = 5
      }

      with<MovementComponent> {}

      with<StateComponent> { state = IDLE }
      with<TypeComponent> { type = MONSTER; subtype = "slime" }

      with<EnemyComponent> { this.name = "slime @ orig ${stageX},${stageY}" }
    }
}
