package com.mygdx.game

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import ktx.collections.GdxArray
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class CollisionSystemTest {


  private val collisionLayer = object : MapLayer() {
    var objects: GdxArray<RectangleMapObject> = GdxArray.with(
      object : RectangleMapObject() {
      }
    )
  }

  private val hazardLayer = object : MapLayer() {
    var objects: GdxArray<RectangleMapObject> = GdxArray.with(
      object : RectangleMapObject() {

      }
    )
  }

  val collSys = CollisionSystem(collisionLayer, hazardLayer)

  @Test
  fun setCorrection_simpleCase_isCorrect() {

  }
}