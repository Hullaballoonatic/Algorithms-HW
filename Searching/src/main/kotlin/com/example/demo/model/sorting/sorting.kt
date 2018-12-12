@file:Suppress("unused", "ClassName")

package com.example.demo.model.sorting

import com.example.demo.model.sorting.divideandconquer.MergeSorter
import com.example.demo.model.sorting.divideandconquer.QuickSorter
import com.example.demo.model.sorting.insertion.InsertionSorter
import com.example.demo.model.sorting.noncomparison.BucketSorter
import com.example.demo.model.sorting.noncomparison.CountingSorter
import com.example.demo.model.sorting.noncomparison.RadixSorter
import com.example.demo.model.sorting.selection.Heap

// helpers
object INFINITY : Comparable<Any> {
    override fun compareTo(other: Any) = 1
}

object NEGATIVE_INFINITY : Comparable<Any> {
    override fun compareTo(other: Any) = -1
}

fun Array<Comparable<Any>>.swap(i: Int, j: Int) {
    val tmp = this[j]
    this[j] = this[i]
    this[i] = tmp
}

fun <T: Any> Array<T>.mapInRange(range: IntRange, transform: (Int) -> T) {
    for (i in range) {
        this[i] = transform(i)
    }
}

infix fun Int.pow(power: Int) = Math.pow(toDouble(), power.toDouble())
infix fun Int.root(root: Int) = Math.pow(toDouble(), 1.0 / root)


// Insertion Sorts
/**
 * Insertion ComparisonSorter
 * sorts the provided collection of comparable objects in increasing value
 *
 * time complexity:     Ω(n), O(n²)
 * space complexity:    1
 * stable:              yes
 *
 */
fun insertionSort(arr: Array<Comparable<Any>>) {
    InsertionSorter.sort(arr)
}


// Selection Sorts
/**
 * Heap Sort
 *
 * time complexity:     O(n*lg(n))
 * space complexity:    Θ(n)
 *
 * stable:              no
 */
fun heapsort(arr: Array<Comparable<Any>>) {
    Heap.sort(arr)
}

fun heapsortAscending(arr: Array<Comparable<Any>>) {
    heapsort(arr)
}

fun heapsortDescending(arr: Array<Comparable<Any>>) {
    Heap.sortDescending(arr)
}


// Divide and Conquer Sorts
/**
 * Merge sort
 * sorts the provided collection of comparable objects in increasing value
 *
 * time complexity:     Θ(n*lg(n))
 * space complexity:    O(n)
 * stable:              yes
 *
 */
fun mergeSort(arr: Array<Comparable<Any>>, start: Int = 0, end: Int = arr.size - 1) {
    MergeSorter.sort(arr, start, end)
}

/**
 * Quick ComparisonSorter
 *
 * time complexity:     Ω(n*log(n)), O(n²)
 * space complexity:    n
 *
 * stable:              No
 */
fun quicksort(arr: Array<Comparable<Any>>, start: Int = 0, end: Int = arr.size - 1) {
    QuickSorter.sort(arr, start, end)
}


// Non-Comparison Sorts
/**
 * Counting Sort
 *
 * time complexity:     Θ(n + k) -> Θ(n)
 * space complexity:    n + k
 *
 * stable:              yes
 */
fun countingSort(arr: IntArray, maxValue: Int = arr.max()!!) = CountingSorter.sort(arr, arr, maxValue)

/**
 * Radix Sort
 *
 * d: number of digits
 * to: base counting system
 *
 * time complexity:     Θ(d(n + to)) -> Θ(d*n)
 * space complexity:    n + d
 *
 * stable:              yes
 */
fun radixsort(arr: IntArray, numDigits: Int = arr.max().toString().length, baseCountingSystem: Int = 10) {
    RadixSorter.sort(arr, numDigits, baseCountingSystem)
}

fun bucketsort(arr: DoubleArray, sorter: ComparisonSorter) {
    BucketSorter.sort(arr, sorter)
}