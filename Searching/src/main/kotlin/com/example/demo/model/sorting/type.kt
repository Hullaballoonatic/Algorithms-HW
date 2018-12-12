@file:Suppress("unused")

package com.example.demo.model.sorting

interface ComparisonSorter {
    fun sort(arr: Array<Comparable<Any>>)
}

interface NonComparisonSorter {
    fun sortOn(arr: IntArray, maxValue: Int = arr.max()!!, digit: Int, baseCountingSystem: Int = 10)

    fun bucketSort(arr: Array<Comparable<Any>>, sorter: ComparisonSorter) = sorter.sort(arr)


}

interface StableSorter