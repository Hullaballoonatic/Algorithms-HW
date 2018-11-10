package com.example.demo.model.sorting

fun MutableList<Int>.insertionSort() {
    var i: Int
    var tmp: Int
    for (j in 1..size) {
        tmp = this[j]
        i = j - 1
        while (i > 0 && this[i] > tmp) {
            this[i + 1] = this[i]
            i--
        }
        this[i + 1] = tmp
    }
}