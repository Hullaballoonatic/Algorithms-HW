@file:Suppress("unused")

package com.example.demo.model.sorting.divideandconquer

import com.example.demo.model.sorting.ComparisonSorter
import com.example.demo.model.sorting.swap

object QuickSorter : ComparisonSorter {
    override fun sort(arr: Array<Comparable<Any>>) = sort(arr, 0, arr.size - 1)

    /**
     * Quick ComparisonSorter
     *
     * time complexity:     Ω(n*log(n)), O(n²)
     * space complexity:    n
     *
     * stable:              No
     */
    fun sort(arr: Array<Comparable<Any>>, start: Int, end: Int) {
        if (start < end) {
            val mid = partition(arr, start, end)
            sort(arr, start, mid - 1)
            sort(arr, mid + 1, end)
        }
    }

    private fun partition(arr: Array<Comparable<Any>>, start: Int, end: Int): Int {
        val mid = arr[end - 1]
        var i = start               // always the final element in the sublist where all e < mid
        for (j in start..(end - 1)) // always the first e in the currently unsorted sublist
            if (arr[j] <= mid)      // if e <= mid put it in the lower partition by swapping
                arr.swap(++i, j)    // otherwise it is incorporated into the upper partition automatically
        arr.swap(i + 1, end - 1)
        return i + 1
    }
}