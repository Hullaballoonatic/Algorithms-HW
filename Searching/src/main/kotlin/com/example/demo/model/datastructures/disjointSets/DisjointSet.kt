@file:Suppress("unused")

package com.example.demo.model.datastructures.disjointSets

import com.example.demo.model.graphs.Graph
import com.example.demo.model.graphs.Vertex
import kotlin.Exception

class DisjointSet<E: Comparable<E>>(representatives: List<E> = listOf()) {
    init {
        representatives.forEach {
            this += it
        }
    }

    val numSets
        get() = sets.size

    private val sets = mutableListOf<HashSet<E>>()

    operator fun plusAssign(v: E) {
        for (set in sets)
            if (v in set) throw Exception("Cannot create a new set from an element already existing in another")
        sets.add(hashSetOf(v))
    }

    operator fun minusAssign(v: E) {
        sets.remove(get(v))
    }

    operator fun get(v: E): Set<E>? {
        for (set in sets)
            if (v in set)
                return set
        return null
    }

    fun union(x: E, y: E) {
        val a = get(x)
        val b = get(y)
        sets.remove(a)
        sets.remove(b)
        val newSet = hashSetOf<E>()
        newSet.addAll(a!!.toList())
        newSet.addAll(b!!.toList())
        sets.add(newSet)
    }
}

fun Graph.toDisjointSet(): DisjointSet<Vertex> {
    val result = DisjointSet<Vertex>()
    for (vertex in vertices)
        result += vertex
    for (edge in edges)
        if (result[edge.from] != result[edge.to])
            result.union(edge.from, edge.to)
    return result
}