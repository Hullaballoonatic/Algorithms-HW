@file:Suppress("unused")

package com.example.demo.model.sorting.noncomparison

import com.example.demo.model.sorting.NonComparisonSorter
import com.example.demo.model.sorting.StableSorter

object CountingSorter : StableSorter, NonComparisonSorter {
    private fun Int.on(digit: Int, baseCountingSystem: Int = 10) = toString()[digit].toString().toInt(baseCountingSystem)

    override fun sortOn(arr: IntArray, maxValue: Int, digit: Int, baseCountingSystem: Int) {
        val result = IntArray(arr.size) { 0 }
        val working = IntArray(maxValue) { 0 }

        for (num in arr) working[num.on(digit, baseCountingSystem)]++

        for (i in 1..maxValue)
            working[i] += working[i-1]

        for (num in arr.reversed())
            result[working[num.on(digit)]--] = num.on(digit)

        for (i in arr.indices)
            arr[i] = result[i]
    }

    /**
     * Counting Sort
     *
     * Linear time sort!
     *
     * time complexity:     Θ(n + k) -> Θ(n)
     * space complexity:    n + k
     *
     * stable:              yes
     */
    fun sort(arr: IntArray, ref: IntArray, maxValue: Int = arr.max()!!) {
        val working = IntArray(maxValue) { 0 }

        // count the amount of each e by incrementing the index of working equal to e
        for (num in arr)
            working[num]++

        // transform working so that each vertex now contains the number of elements that precede it
        for (i in 1..maxValue)
            working[i] += working[i-1]

        // go backwards set the largest element of our result equal to the largest value of from, and decrement the amount of that element every time
        for (num in arr.reversed())
            ref[working[num]--] = num
    }
}

