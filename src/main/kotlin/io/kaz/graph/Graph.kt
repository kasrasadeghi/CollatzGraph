package io.kaz.graph

/**
 * Created by kasra on 6/6/17.
 */

class Graph<T, EdgeType> {
  var nodes: HashMap<T, Node<T, EdgeType>> = HashMap()
  var edges: Set<Edge<T, EdgeType>> = HashSet()

  data class Node<T, EdgeType>(var value: T) {
    var adj: Set<Node<T, EdgeType>> = HashSet()
  }

  data class Edge<T, EdgeType>(
      val left: Node<T, EdgeType>,
      val right: Node<T, EdgeType>,
      var type: EdgeType? = null)

  operator fun plusAssign(value: T) {
    if (null == nodes[value]) nodes.put(value, Node(value))
  }

  /**
   * Connects left to right with an edge of type 'type'.
   *
   * Creates a node if it does not exist.
   */
  fun connect(left: T, right: T, type: EdgeType) {
    this += left; this += right
    val leftNode = nodes[left]!!
    val rightNode = nodes[right]!!

    edges += Edge(leftNode, rightNode, type)

    leftNode.adj += rightNode
    rightNode.adj += leftNode
  }
}
