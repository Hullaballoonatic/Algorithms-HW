package com.example.demo.model.datastructures.bst

import com.example.demo.model.datastructures.bst.WalkOrder.*

open class BSTNode<E: Comparable<E>>(val value: E, parent: BSTNode<E>? = null) {
    val isRoot get() = parent == null
    val isLeftChild get() = parent?.left == this
    val isRightChild get() = parent?.right == this
    val isLeaf get() = children.isEmpty()

    val size: Int get() = children.sumBy { it.size } + 1

    val rank: Int get() = (left?.size ?: 0) + 1

    var parent: BSTNode<E>? = parent
        set(v) {
            if (v != this) {
                field = v
                if (isLeftChild && v?.left != this) v?.left = this
                if (isRightChild && v?.right != this) v?.right = this
            }
        }

    var left: BSTNode<E>? = null
        set(v) {
            if (v != this) {
                field = if (v == null || v.value <= value) v else throw Exception("Left child value must be less than parent value")
                if (v?.parent != this) v?.parent = this
            }
        }

    var right: BSTNode<E>? = null
        set(v) {
            if (v != this) {
                field = if (v == null || v.value >= value) v else throw Exception("Right child value must be greater than parent value")
                if (v?.parent != this) v?.parent = this
            }
        }

    val children get() = mutableListOf<BSTNode<E>>().apply {
        if (left != null) add(left!!)
        if (right != null) add(right!!)
    }

    val sibling get() = when {
        isRightChild -> parent?.left
        isLeftChild -> parent?.right
        else -> null
    }

    val uncle get() = parent?.sibling

    val grandparent get() = parent?.parent

    val height: Int get() = 1 + (children.map{ it.height }.max() ?: 0)

    val max: BSTNode<E> get() = if (right != null) right!!.max else this
    val min: BSTNode<E> get() = if (left != null) left!!.min else this

    val predecessor: BSTNode<E>? get() {
        return if (left != null) {
            left!!.max
        } else {
            var cur: BSTNode<E>? = this
            while (cur!!.isLeftChild)
                cur = cur.parent
            cur.parent
        }
    }

    val successor: BSTNode<E>? get() {
        return if (right != null) {
            right!!.min
        } else {
            var cur: BSTNode<E>? = this
            while (cur!!.isRightChild)
                cur = cur.parent
            return cur.parent
        }
    }

    operator fun contains(v: E) = get(v) != null
    operator fun contains(node: BSTNode<E>) = get(node.value) != null

    operator fun get(v: E): BSTNode<E>? {
        return when {
            v > value -> right?.get(v)
            v < value -> left?.get(v)
            else -> this
        }
    }

    fun walk(order: WalkOrder = IN_ORDER): List<BSTNode<E>> {
        return mutableListOf<BSTNode<E>>().also {
            when (order) {
                IN_ORDER -> {
                    if (left != null) it.addAll(left!!.walk(order))
                    it.add(this)
                    if (right != null) it.addAll(right!!.walk(order))
                }
                PRE_ORDER -> {
                    it.add(this)
                    if (left != null) it.addAll(left!!.walk(order))
                    if (right != null) it.addAll(right!!.walk(order))
                }
                POST_ORDER -> {
                    if (left != null) it.addAll(left!!.walk(order))
                    if (right != null) it.addAll(right!!.walk(order))
                    it.add(this)
                }
            }
        }
    }

    fun delete() {
        parent = null
        left = null
        right = null
    }

    fun copy(value: E = this.value, parent: BSTNode<E>? = this.parent, left: BSTNode<E>? = this.left, right: BSTNode<E>? = this.right): BSTNode<E> {
        return BSTNode(value).apply {
            this.parent = parent?.copy()
            this.left = left?.copy()
            this.right = right?.copy()
        }
    }
}