package io.kaz.view

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

/**
 * Created by kasra on 6/8/17.
 */
class GraphViewKtTest {
  @Test fun testGrid() {
    val grid = GraphView.Grid()
    grid[5, 5] = GraphView.Pointer(5.b, 5.b)
    grid[4, 6] = GraphView.Pointer(4.b, 6.b)
    grid[5, 6] = GraphView.Pointer(5.b, 6.b)

    assertTrue(grid.keys.contains(Pair(5, 5)))
    assertTrue(grid.keys.contains(Pair(4, 6)))
    assertTrue(grid.keys.contains(Pair(5, 6)))
    assertFalse(grid.keys.contains(Pair(2, 2)))
  }
}