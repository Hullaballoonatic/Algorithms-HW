@file:Suppress("unused")

package com.example.demo.model.sorting.insertion

import com.example.demo.model.sorting.ComparisonSorter
import com.example.demo.model.sorting.StableSorter

object InsertionSorter : ComparisonSorter, StableSorter {
    /**
     * Insertion ComparisonSorter
     * sorts the provided collection of comparable objects in increasing value
     *
     * time complexity:     Ω(n), O(n²)
     * space complexity:    1
     * stable:              yes
     *
     */
    override fun sort(arr: Array<Comparable<Any>>) {
        for (j in 2..arr.size) {
            val key = arr[j]
            var i = j
            while (arr[--i] > key && i > 0)
                arr[i + 1] = arr[i]
            arr[i + 1] = key
        }
    }
}