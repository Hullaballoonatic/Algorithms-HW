@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package com.example.demo.model.sorting.noncomparison

import com.example.demo.model.sorting.ComparisonSorter
import com.example.demo.model.sorting.NonComparisonSorter

object BucketSorter : NonComparisonSorter {
    override fun sortOn(arr: IntArray, maxValue: Int, digit: Int, baseCountingSystem: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun sort(arr: DoubleArray, sorter: ComparisonSorter) {
        val buckets = arr.toBuckets()
        for (bucket in buckets)
            sorter.sort(arr.toTypedArray() as Array<Comparable<Any>>)
        val result = buckets.toDoubleArray()
        for (i in arr.indices)
            arr[i] = result[i]
    }

    fun DoubleArray.toBuckets(): List<List<Double>> {
        val result = List<ArrayList<Double>>(size) { arrayListOf() }
        for (number in this)
            result[(size * number).toInt()] += number
        return result
    }

    fun List<List<Double>>.toDoubleArray(): DoubleArray = flatten().toDoubleArray()
}