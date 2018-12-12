@file:Suppress("unused")

package com.example.demo.model.sorting.noncomparison

import com.example.demo.model.sorting.pow

object RadixSorter {
    fun sort(arr: IntArray, numDigits: Int = arr.max().toString().length, baseCountingSystem: Int = 10) {
        val a = IntArray(baseCountingSystem)
        for (digit in 0 until numDigits) {
            a.mapIndexed { i, _ -> (arr[i] / (baseCountingSystem pow digit)).toInt() % baseCountingSystem }
            CountingSorter.sort(a, arr, baseCountingSystem - 1)
        }
    }
}