@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.example.demo.model.datastructures.hashtables

import kotlin.math.sqrt

/**
 * Division Method
 *
 * self explanatory
 *
 * if using this method, the best table sizes are prime numbers not too close to from power of 2
 */
class DivisionMethod(val m: Int) : (Int) -> Int {
    override fun invoke(k: Int) = k % m
}

/**
 * Multiplication Method
 * m: hash table size
 * k: value to be hashed
 * A: 0 < A < 1
 * h(k) = ⌊m(kA % 1)⌋
 *
 * multiply k by A and mod 1 to get just the fractional component.
 * multiply that by m and take only the integer part as the hashed key
 */
class MultiplicationMethod(val scalar: Double = (sqrt(5.0) - 1.0) / 2.0, val m: Int) : (Int) -> Int {
    override fun invoke(k: Int): Int = (m * ((k * scalar) % 1)).toInt()
}

class OpenAddressedHashTable<E: Any>(val size: Int, val h: (Int) -> Int = DivisionMethod(size)) {
    private val hashTable = MutableList<E?>(size) { null }

    fun insertUsing(v: E, h: (Int, Int) -> Int) : Int {
        val hashedValue = v.hashCode()
        for (i in 0..size) {
            val k = h(hashedValue, i)
            if (hashTable[k] == null) {
                hashTable[k] = v
                return k
            }
        }
        throw Exception("Hash Table Overflow")
    }

    fun deleteUsing(v: E, h: (Int, Int) -> Int) : Int {
        val k = searchUsing(v, h) ?: throw Exception("Value not found in Table")
        hashTable[k] = null
        return k
    }

    fun searchUsing(v: E, h: (Int, Int) -> Int) : Int? {
        val hashedValue = v.hashCode()
        for (i in 0..size) {
            val k = h(hashedValue, i)
            if (hashTable[k] in listOf(v, null))
                return k
        }
        return null
    }

    operator fun minusAssign(v: E) {
        deleteUsing(v, linearProbing)
    }

    operator fun plusAssign(v: E) {
        insertUsing(v, linearProbing)
    }

    operator fun get(k: Int) = hashTable[k]

    operator fun set(from: E, to: E) {
        val k = searchUsing(from, linearProbing)
        if (k != null) hashTable[k] = to else throw Exception("Value not found in Table")
    }

    val linearProbing: ((Int, Int) -> Int) = { k, i ->
        (h(k) + i) % size
    }

    fun quadraticProbing(c1: Int = 1, c2: Int = 1): ((Int, Int) -> Int) = { k, i ->
        h(k) + c1*i + c2*i*i
    }

    fun doubleHashing(h1: ((Int) -> Int), h2: ((Int) -> Int)): ((Int, Int) -> Int) = { k, i ->
        (h1(k) + i * h2(k)) % size
    }
}