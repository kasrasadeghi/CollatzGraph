package io.kaz.view

import io.kaz.app.collapze
import javafx.application.Platform
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.control.ScrollPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.math.BigInteger
import java.util.*

/**
 * Created by kasra on 6/6/17.
 */
//region val Int.b: BigInteger ...

val Int.b: BigInteger
  get() = BigInteger.valueOf(this.toLong())

operator fun BigInteger.times(other: Int): BigInteger {
  return this * other.b
}

operator fun BigInteger.plus(other: Int): BigInteger {
  return this + other.b
}

operator fun BigInteger.div(other: Int): BigInteger {
  return this / other.b
}

operator fun BigInteger.rem(other: Int): BigInteger {
  return this % other.b
}

//endregion

class GraphView : View("Collatz Graph Viewer") {
  //TODO make this part of the model with properties and stuff
  var fontSize = 10.0
  var radius = fontSize * 2

  //TODO maybe make animations and stuff so it's pretty

  /**
   * The next region documents the bubble builder, which is used as a shortcut
   * for a circle and some text centered in it.
   */
  //region builder -> fun bubble(...) { ... }
  fun EventTarget.bubble(initialValue: String? = null, cx: Double, cy: Double) {
    val rad = fontSize + 5

    opcr(this, Circle(), {
      fill = Color.WHITE
      centerX = cx
      centerY = cy
      radius = rad
      stroke = Color.BLUE
      strokeWidth = 2.0
    })
    opcr(this, stackpane {
      layoutX = cx - fontSize
      layoutY = cy - fontSize
      prefWidth = fontSize * 2
      prefHeight = fontSize * 2

      val text = text(initialValue) {
        font = Font.font("Monospaced", fontSize)
        fill = Color.BLUE
        textAlignment = TextAlignment.CENTER
      }
      this.add(text)
      StackPane.setAlignment(text, Pos.CENTER)
    })
  }
  //endregion

  override val root = borderpane {
    shortcut("Esc") {
      Platform.exit()
    }

    center = makeFrame()
    top = hbox {
      textfield(fontSize.toString()) {
        action {
          fontSize = text.toDouble()
          radius = fontSize * 2
          text = ""
          center = makeFrame()
        }
      }
    }
  }

  /**
   * The function that makes the centerpiece of the application using the current state of the global variables.
   *
   * Should be deprecated when using a model because of dynamic updating and binding, but this might just be easier.
   * And less glitchy.
   * And less laggy.
   */
  fun makeFrame(): StackPane {
    return stackpane {
      scrollpane {
        isPannable = true
        this.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        this.vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        group {
          val list = collapze(27)
          val grid = makeGrid(list)

          rectangle {
            fill = Color.GREY
            x = 0.0
            y = 0.0
            width = radius * 1.5 * grid.maxX + (fontSize * 2)
            height = radius * 1.5 * grid.maxY + (fontSize * 2)
          }

          for ((x, y) in grid.keys) {
            bubble(grid[x, y].toString(), fontSize + radius * 1.5 * x, fontSize + radius * 1.5 * y)
          }
        }

      }
    }
  }

  /**
   * Using a Pointer instead of a regular ol' BigInteger because I will want to backtrack where the nodes are from in
   * the future, especially in order to make leaping connections when more than one branch comes out of a horiztonal
   * doubling series.
   */
  data class Pointer(val value: BigInteger, val to: BigInteger) {
    override fun toString(): String {
      return "$value"
    }
  }

  /**
   * The Grid class is basically just a 2d array of Pointers.
   *
   * It uses a sparse Hashmap instead of an actual 2d array with null values to conserve on memory,
   * but I'm not sure if it actually helps much because HashMaps themselves are rather sparse.
   */
  class Grid {
    var grid = HashMap<Int, HashMap<Int, Pointer>>()

    val keys: List<Pair<Int, Int>>
      get() {
        val list = grid.keys.map { x ->
          grid[x]?.keys?.map { y ->
            Pair(x, y)
          }
        }.reduceRight(fun(list: List<Pair<Int, Int>>?, acc: List<Pair<Int, Int>>?): List<Pair<Int, Int>> {
          val result = ArrayList<Pair<Int, Int>>()
          if (null != list) {
            result.addAll(list)
          }
          if (null != acc) {
            result.addAll(acc)
          }

          return result
        })
        return list ?: ArrayList()
      }
    var maxX = 1
    var maxY = 1

    operator fun set(x: Int, y: Int, p: Pointer) {
      if (x > maxX) maxX = x
      if (y > maxY) maxY = y

      if (grid[x] == null) {
        val a = HashMap<Int, Pointer>()
        grid[x] = a
        a[y] = p
      } else {
        grid[x]!![y] = p
      }
    }

    operator fun get(x: Int, y: Int): Pointer? {
      return grid[x]?.get(y)
    }

    override fun toString(): String {
      var str = ""
      for (x in 0..maxX) {
        for (y in 0..maxY) {
          if (grid[x]?.get(y) == null) {
            str += "(null)"
          } else {
            str += grid[x]!![y]
          }
        }
        str += "\n"
      }
      return str
    }
  }

  /**
   * Makes a grid out of a list of BigIntegers
   *
   * Does most of the legwork for figuring out whether things should be horizontally or vertically neighboring.
   * The rest of the legwork is done by the list's ordering.
   */
  fun makeGrid(rlist: List<BigInteger>): Grid {
    val result = Grid()
    val list = rlist.asReversed()
    var prev = BigInteger.ONE
    var x = 0
    var y = 0
    for (num in list) {
      if (num == BigInteger.ONE) {
        result[x, y] = Pointer(num, num)
        continue
      }
      if (num == prev * 2) {
        ++x
        result[x, y] = Pointer(num, prev)
        prev = num
      } else {
        ++y
        result[x, y] = Pointer(num, prev)
        prev = num
      }
    }

    return result
  }
}

