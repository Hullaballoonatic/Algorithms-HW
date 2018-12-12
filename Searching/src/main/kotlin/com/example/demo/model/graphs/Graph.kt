@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.example.demo.model.graphs

import com.example.demo.model.datastructures.disjointSets.DisjointSet
import java.util.PriorityQueue
import java.util.Queue
import java.util.Stack
import kotlin.math.min

class Graph(val vertices: List<Vertex>, val edges: List<Edge>, val dag: Boolean = false) {
    var isShortestPathTree: Boolean = false

    private val weights = HashMap<Vertex, MutableMap<Vertex, Double>>().apply {
        for (vertex in vertices) put(vertex, HashMap())
        for (edge in edges) {
            get(edge.from)!![edge.to] = edge.weight ?: Double.MAX_VALUE
            if (!edge.directed) get(edge.to)!![edge.from] = edge.weight ?: Double.MAX_VALUE
        }
    }

    private fun weight(a: Vertex, b: Vertex): Double = weights[a]?.get(b) ?: Double.MAX_VALUE

    private val adjListRepresentation: Map<Vertex, List<Vertex>>
        get() = HashMap<Vertex, ArrayList<Vertex>>().apply {
            for (vertex in vertices) put(vertex, ArrayList())
            for (edge in edges) {
                get(edge.from)!!.add(edge.to)
                if (!edge.directed) get(edge.to)!!.add(edge.from)
            }
        }

    private val adjMatrixRepresentation: Map<Vertex, MutableMap<Vertex, Double>>
        get() = HashMap<Vertex, MutableMap<Vertex, Double>>().apply {
            for (vertex in vertices) {
                put(vertex, vertices.zip(List(vertices.size) { 0.0 }).toMap() as MutableMap<Vertex, Double>)
            }
            for (edge in edges) {
                get(edge.from)!![edge.to] = edge.weight!!
                if (!edge.directed) get(edge.from)!![edge.to] = edge.weight
            }
        }

    private val pathExists: Map<Vertex, MutableMap<Vertex, Boolean>>
        get() = transitiveClosure()

    // ELEMENTARY GRAPH --------------------------------------
    fun dfs(start: Vertex) {
        val adjList = adjListRepresentation
        var cur = start
        cur.distance = 0.0
        val s = Stack<Vertex>()
        val v = HashSet<Vertex>()
        s += cur
        v += cur
        while (s.isNotEmpty()) {
            cur = s.pop()
            for (nxt in adjList[cur]!!) {
                if (v.add(nxt)) {
                    nxt.parent = cur
                    nxt.distance += cur.distance
                    s += nxt
                }
            }
        }
    }

    fun bfs(start: Vertex) {
        var cur = start
        cur.distance = 0.0
        val q = PriorityQueue<Vertex> { a, b -> a.distance.compareTo(b.distance) }
        val v = HashSet<Vertex>()
        q += cur
        v += cur
        while (q.isNotEmpty()) {
            cur = q.poll()
            for (nxt in adjListRepresentation[cur]!!) {
                if (v.add(nxt)) {
                    nxt.distance += cur.distance
                    nxt.parent = cur
                    q += nxt
                }
            }
        }
    }
    // ELEMENTARY GRAPH --------------------------------------

    // SINGLE SOURCE SHORTEST PATH ---------------------------
    fun relax(a: Vertex, b: Vertex, w: Double) {
        if (b.distance > a.distance + w) {
            b.distance = a.distance + w
            b.parent = a
        }
    }

    fun resetOn(v: Vertex) {
        vertices.forEach { it.reset() }
        v.distance = 0.0
    }

    /**
     * relax everything
     * check for negative cycles
     * time complexity: Θ(V*E)
     */
    fun bellmanFord(start: Vertex = vertices[0]): Boolean {
        resetOn(start)
        for (a in vertices) {
            for (b in adjListRepresentation[a]!!) {
                relax(a, b, weight(a, b))
            }
        }
        return if (dag) true else
            edges.none { e ->
                e.to.distance > (e.from.distance + (e.weight ?: Double.MAX_VALUE))
            }
    }

    /**
     * greedily move forward, avoiding any you've already been to (using queue) and relaxing at each step
     */
    fun dijkstra(start: Vertex, weight: (Vertex, Vertex) -> Double = { from, to -> this.weight(from, to)}) {
        val adjList = adjListRepresentation
        resetOn(start)
        val q: Queue<Vertex> = PriorityQueue<Vertex> { a, b -> (a.distance - b.distance).toInt() }.apply {
            addAll(vertices)
        }
        while (q.isNotEmpty()) {
            val cur = q.poll()
            for (nxt in adjList[cur]!!)
                relax(cur, nxt, weight(cur, nxt))
        }
    }
    // SINGLE SOURCE SHORTEST PATH ---------------------------

    // MST ---------------------------------------------------
    /**
     * MST by Kruskal Algorithm
     */
    val minimumSpanningTree: List<Edge> get() = kruskalMST()

    fun kruskalMST(): List<Edge> {
        val djs = DisjointSet(vertices)
        val result = ArrayList<Edge>()
        edges.sortedBy { o -> o.weight }.forEach { e ->
            if (djs.numSets == 1)
                if (djs[e.from] != djs[e.to]) {
                    result += e
                    djs.union(e.from, e.to)
                }
        }
        return result
    }

    fun primMST(root: Vertex) {
        val adjList = adjListRepresentation
        resetOn(root)
        val q: Queue<Vertex> = PriorityQueue<Vertex> { a, b -> (a.distance - b.distance).toInt() }.apply {
            addAll(vertices)
        }
        while (q.isNotEmpty()) {
            val cur = q.poll()
            for (nxt in adjList[cur]!!) {
                val w = weight(cur, nxt)
                if (nxt in q && w < nxt.distance) {
                    nxt.parent = cur
                    nxt.distance = w
                }
            }
        }
    }
    // MST ---------------------------------------------------

    // All-Pairs Shortest Path -------------------------------
    private val minWeightByNumEdges: List<Map<Vertex, MutableMap<Vertex, Double>>> = List(vertices.size) {
        HashMap<Vertex, HashMap<Vertex, Double>>().apply {
            for (from in vertices) {
                put(from, HashMap())
                for (to in vertices) {
                    get(from)!![to] = if (from == to) 0.0 else Double.MAX_VALUE
                }
            }
        }
    }

    /**
     * time-complexity:     Θ(n³㏒n)
     */
    private fun allPairsShortestPathMatrix() {
        minWeightByNumEdges.apply {
            var m = 1
            while(m < vertices.size) {
                for (i in vertices) {
                    for (j in vertices) {
                        for (k in vertices) {
                            get(2 * m - 2)[i]!![j] = min(this[m - 1][i]!![j]!!, this[m - 1][i]!![k]!! + weight(k, j))
                        }
                    }
                }
                m *= 2
            }
        }
    }

    private fun transitiveClosure(): Map<Vertex, MutableMap<Vertex, Boolean>> {
        val result = HashMap<Vertex, MutableMap<Vertex, Boolean>>().apply {
            for (from in vertices) {
                for (to in vertices) {
                    get(from)!![to] = from == to
                }
            }
        }
        for (mid in vertices) {
            for (from in vertices) {
                for (to in vertices) {
                    result[from]!![to] = result[from]!![to]!! || (result[from]!![mid]!! && result[mid]!![to]!!)
                }
            }
        }
        return result
    }

    /**
     * time complexity:     Θ(n³)
     * fastest for dense graphs
     */
    private fun floydWarshall() {
        for (mid in vertices) {
            for (from in vertices) {
                for (to in vertices) {
                    adjMatrixRepresentation[from]!![to] = min(adjMatrixRepresentation[from]!![to]!!, adjMatrixRepresentation[from]!![mid]!! + adjMatrixRepresentation[mid]!![to]!!)
                }
            }
        }
    }

    fun printAllPairsShortestPath(from: Vertex, to: Vertex) {
        if (!isShortestPathTree) dijkstra(from)
        when {
            from == to        -> println(from)
            to.parent == null -> println("no path from $from to $to exists.")
            else              -> {
                printAllPairsShortestPath(from, to.parent!!)
                println(to)
            }
        }
    }

    /**
     * runtime complexity:      O(V²㏒V + VE)
     * better for sparse graphs
     *
     * re-weight everything, then run dijkstra once for each vertex
     */
    private fun johnson() {
        // reweigh everything
        for (vertex in vertices) {
            // TODO:
            // create new other vertex
            //
        }
        // run bellman-ford to ensure no negative cycles
        if (!bellmanFord()) throw Exception("Cannot run Johnson Algorithm on graph with negative cycles")
        // now calculate all weights by  weight + reweightedFrom - reweightedTo
        val weight: (Vertex, Vertex) -> Double = { from, to ->
            weight(from, to) + from.distance - to.distance
        }
        for (from in vertices) {
            for (to in adjListRepresentation[from]!!) {
                adjMatrixRepresentation[from]!![to] = weight(from, to) + from.distance - to.distance
            }
        }
        // run dijkstra once for every vertex
        for (from in vertices) {
            dijkstra(from, weight)
            // un-reweigh everything
            for (to in vertices) {
                adjMatrixRepresentation[from]!![to] = weight(from, to) + to.distance - from.distance
            }
        }

    }
    // All-Pairs Shortest Path -------------------------------
}

data class Edge(val from: Vertex, val to: Vertex, val weigh: (Vertex, Vertex) -> Double? = { _, _ -> null },
                val weight: Double? = weigh(from, to), val directed: Boolean = false)

data class Vertex(val id: Int, var parent: Vertex? = null,
                  var distance: Double = Double.MAX_VALUE) : Comparable<Vertex> {
    override fun compareTo(other: Vertex): Int = id.compareTo(other.id)

    fun reset() {
        parent = null
        distance = Double.MAX_VALUE
    }

    override fun toString() = "$id : $distance"
}