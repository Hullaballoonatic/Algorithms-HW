@file:Suppress("unused")

package com.example.demo.model.sorting.divideandconquer

import com.example.demo.model.sorting.ComparisonSorter
import com.example.demo.model.sorting.INFINITY
import com.example.demo.model.sorting.StableSorter
import com.example.demo.model.sorting.mapInRange

object MergeSorter: StableSorter, ComparisonSorter {
    /**
     * Merge sort
     * sorts the provided collection of comparable objects in increasing value
     *
     * time complexity:     Θ(n*lg(n))
     * space complexity:    O(n)
     * stable:              yes
     *
     */
    fun sort(arr: Array<Comparable<Any>>, start: Int, end: Int) {
        if (start < end) {
            val mid = (start + end) / 2
            sort(arr, start, mid)
            sort(arr,mid + 1, end)
            merge(arr, start, mid, end)
        }
    }

    override fun sort(arr: Array<Comparable<Any>>) = sort(arr, 0, arr.size - 1)

    /**
     * merge
     *
     * time complexity:     Θ(n), where n = end - start + 1
     * stable:              yes
     *
     * Called object is assumed to two sorted arrays from start..halfway and halfway + 1..end
     */
    private fun merge(arr: Array<Comparable<Any>>, start: Int, mid: Int, end: Int) {
        // Create the sub-arrays
        val left = arr.asList().subList(start, mid) + INFINITY
        val right = arr.asList().subList(mid, end - 1) + INFINITY

        // initial indices of left and right, respectively
        var l = 0
        var r = 0

        arr.mapInRange(start until end) {
            if (left[l] <= right[r])
                left[l++]
            else right[r++]
        }
    }
}