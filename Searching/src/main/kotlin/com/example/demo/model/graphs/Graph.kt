package com.example.demo.model.graphs

import com.example.demo.model.graphs.GraphRepresentationType.ADJACENCY_LIST
import com.example.demo.model.graphs.GraphRepresentationType.ADJACENCY_MATRIX

enum class GraphRepresentationType {
    ADJACENCY_LIST,
    ADJACENCY_MATRIX
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Graph<ID: Comparable<Any>, W: Comparable<Any>>(val vertices: List<Vertex<ID>>, val edges: List<Edge<Vertex<ID>, W>>, type: GraphRepresentationType, directed: Boolean = false) {
    data class AdjListEdge<W: Comparable<Any>>(val to: Int, val weight: W)
    val edgeRepresentation = when(type) {
        ADJACENCY_LIST -> {
            List(vertices.size) { ArrayList<AdjListEdge<W>>() }.apply {
                for (edge in edges) {
                    val u = vertices.indexOf(edge.u)
                    val v = vertices.indexOf(edge.v)

                    get(u) += AdjListEdge(v, edge.weight)

                    if (!directed) get(v).add(AdjListEdge(u, edge.weight))
                }
            }
        }
        ADJACENCY_MATRIX -> {
            List(vertices.size) { MutableList<W?>(vertices.size) { null } }.apply {
                for (edge in edges) {
                    val u = vertices.indexOf(edge.u)
                    val v = vertices.indexOf(edge.v)

                    get(u)[v] = edge.weight

                    if (!directed) get(v)[u] = edge.weight
                }
            }
        }
    }
}

interface Vertex<ID: Comparable<Any>> : Comparable<Vertex<ID>> {
    val id: ID
    override fun compareTo(other: Vertex<ID>) = id.compareTo(other.id)
}

interface Edge<V: Vertex<*>, W: Comparable<Any>> : Comparable<Edge<V, W>> {
    val u: V
    val v: V
    val weight: W
    override fun compareTo(other: Edge<V, W>) = weight.compareTo(other.weight)
}