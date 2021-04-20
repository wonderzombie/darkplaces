package com.mygdx.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor

internal class PlayerComponent : Component
internal class ActorComponent(val actor: Actor) : Component
internal class AnimationComponent(val animation: Animation<AtlasRegion>) : Component
