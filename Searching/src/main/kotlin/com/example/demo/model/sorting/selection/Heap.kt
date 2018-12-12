@file:Suppress("unused")

package com.example.demo.model.sorting.selection

import com.example.demo.model.sorting.ComparisonSorter
import com.example.demo.model.sorting.selection.Heap.Type.*
import com.example.demo.model.sorting.swap
import java.lang.Exception
import kotlin.math.ceil

@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "MemberVisibilityCanBePrivate")
class Heap(var arr: Array<Comparable<Any>>, type: Heap.Type = MAX) : ComparisonSorter {
    init {
        build()
    }

    var type = type
        set(v) {
            field = v
            build()
        }

    var size = arr.size
    val last get() = size - 1

    fun reset() {
        size = arr.size
    }

    operator fun Comparable<Any>.compareTo(b: Comparable<Any>) = type.comparator.compare(this, b)

    private val root get() = arr[0]

    val Int.hasL get() = l < size
    val Int.hasR get() = r < size

    fun hasL(i: Int) = l(i) < size
    fun hasR(i: Int) = r(i) < size

    fun parent(i: Int) = arr[p(i)]
    fun left(i: Int)   = arr[l(i)]
    fun right(i: Int)  = arr[r(i)]

    /**
     * Heapify
     *
     * time complexity:     O(lg(n))
     */
    private fun heapify(i: Int) {
        val contenders = arrayListOf(i).apply {
            if (i.l < size) add(i.l)
            if (i.r < size) add(i.r)
        }

        val p = contenders.maxBy { arr[it] } ?: i

        if (p != i) {
            arr.swap(i, p) // swap the max of [i], [l], [r] into the parent position if it isn't already
            heapify(p)
        }
    }

    /**
     * heapify at every node working backwards from halfway
     * time complexity:     Θ(n)
     */
    private fun build() {
        for (i in (size / 2) - 1 downTo 0)
            heapify(i)
    }

    /**
     * time complexity:     O(lg(n))
     * space complexity:    Θ(n)
     */
    fun max(i: Int = 0) = when(type) {
        MAX -> arr[i]
        MIN -> arr.max()!!
    }
    val max get(): Comparable<Any> = max()
    fun maxIndex(i: Int = 0) = when(type) {
        MAX -> i
        MIN -> arr.indexOf(max)
    }
    val maxIndex get() = maxIndex()

    /**
     * time complexity:     O(lg(n))
     * space complexity:    Θ(n)
     */
    fun min(i: Int = 0) = when(type) {
        MAX -> arr.min()!!
        MIN -> arr[i]
    }
    val min get(): Comparable<Any> = min()
    fun minIndex(i: Int = 0) = when(type) {
        MAX -> arr.indexOf(min)
        MIN -> i
    }
    val minIndex get() = minIndex()

    /**
     * time complexity:     O(lg(n))
     * space complexity:    Θ(n)
     */
    operator fun plus(v: Comparable<Any>): Heap {
        val data = arr + v
        return Heap(data)
    }

    operator fun plusAssign(v: Comparable<Any>) {
        arr += v
        fixUp(size++)
    }

    fun fixUp(i: Int) {
        if (i > 0 && parent(i) < arr[i]) {
            arr.swap(i, i.p)
            fixUp(i.p)
        }
    }

    fun fixDown(i: Int) {
        val contenders = arrayListOf(i).apply {
            if (i.hasL) add(i.l)
            if (i.hasR) add(i.r)
        }
        when(contenders.minBy { arr[it] }) {
            i -> return
            i.l -> {
                arr.swap(i.l, i)
                fixDown(i.l)
            }
            i.r -> {
                arr.swap(i.r, i)
                fixDown(i.r)
            }
        }
    }

    operator fun minus(v: Comparable<Any>): Heap {
        delete(arr.indexOf(v))
        return Heap(arr)
    }

    operator fun minusAssign(v: Comparable<Any>) {
        delete(arr.indexOf(v))
        size--
    }

    /**
     * time complexity:     O(lg(n))
     * space complexity:    Θ(n)
     */
    fun delete(i: Int): Comparable<Any> {
        val result = arr[i]
        arr.swap(i, --size)
        fixUp(i)
        fixDown(i)
        return result
    }

    fun extractRoot(): Comparable<Any> {
        if (size < 0) throw UnderFlowException("Extract Root")
        val root = root
        this[0] = arr[size--]
        heapify(last)
        return root
    }

    /**
     * Increase / Decrease Key
     *
     * time complexity:     O(lg(n))
     */
    operator fun set(i: Int, v: Comparable<Any>) {
        if (type == MAX && v < arr[i]) throw KeyException(type, "Increase Key")
        if (type == MIN && v > arr[i]) throw KeyException(type, "Decrease Key")
        arr[i] = v
        fixUp(i)
    }


    /**
     * Heap Sort
     *
     * time complexity:     O(n*lg(n))
     * space complexity:    Θ(n)
     *
     * stable:              no
     */
    override fun sort(arr: Array<Comparable<Any>>) {
        for (i in size - 1 downTo 1) {
            arr.swap(0, i)     // put the largest element at the end of the list
            size--              // decrement size to no longer consider the sorted elements at the end
            if(type == MAX) arr.maxHeapify(i)           // fixUp the heap so largest value at top again
            else arr.minHeapify(i)
        }
    }

    fun sort() = sort(arr)

    enum class Type(val comparator: Comparator<Comparable<Any>>) {
        // for all elements
        // A[parent(i)] >= A[i]
        // root is largest element
        MAX(Comparator<Comparable<Any>> { a, b ->
            when {
                a > b -> 1
                a < b -> -1
                else -> 0
            }
        }),
        // A[parent(i)] <= A[i]
        // root is smallest element
        MIN(Comparator<Comparable<Any>> { a, b ->
            when {
                a < b -> 1
                a > b -> -1
                else -> 0
            }
        })
    }


    companion object HeapSorter : ComparisonSorter {
        val Array<Comparable<Any>>.root get() = this[0]

        fun p(i: Int) = ceil((i / 2.0)).toInt() - 1
        fun l(i: Int) = 2 * i + 1
        fun r(i: Int) = 2 * i + 2

        val Int.p get() = ceil((this / 2.0)).toInt() - 1
        val Int.l get() = 2 * this + 1
        val Int.r get() = 2 * this + 2

        private fun Array<Comparable<Any>>.maxHeapify(i: Int = 0) {
            val contenders = arrayListOf(i).apply {
                if (i.l < size) add(i.l)
                if (i.r < size) add(i.r)
            }

            val p = contenders.maxBy { this[it] } ?: i

            if (p != i) {
                swap(i, p)
                maxHeapify(p)
            }
        }

        fun Array<Comparable<Any>>.minHeapify(i: Int = 0) {
            val contenders = arrayListOf(i).apply {
                if (i.l < size) add(i.l)
                if (i.r < size) add(i.r)
            }

            val p = contenders.minBy { this[it] } ?: i

            if (p != i) {
                swap(i, p)
                minHeapify(p)
            }
        }

        fun Array<Comparable<Any>>.toMaxHeap() {
            for (i in (size / 2) - 1 downTo 0)
                maxHeapify(i)
        }

        fun Array<Comparable<Any>>.toMinHeap() {
            for (i in (size / 2) - 1 downTo 0)
                minHeapify(i)
        }

        override fun sort(arr: Array<Comparable<Any>>) {
            Heap(arr).sort(arr)
        }

        fun sortDescending(arr: Array<Comparable<Any>>) {
            Heap(arr, MIN).sort(arr)
        }
    }

    class KeyException(type: Heap.Type, operationName: String) : Exception("Occurred during $operationName. $type Heaps require that key value only be ${if (type == MAX) "increased" else "decreased"}")

    class UnderFlowException(operationName: String) : Exception("Occurred during $operationName.\n")
}
