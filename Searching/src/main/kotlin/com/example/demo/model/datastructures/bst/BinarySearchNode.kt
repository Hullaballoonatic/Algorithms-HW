package com.example.demo.model.datastructures.bst

import com.example.demo.model.datastructures.bst.WalkOrder.*

data class BinarySearchNode(val value: Int, var parent: BinarySearchNode? = null) {
    private val self = listOf(this)

    var right: BinarySearchNode? = null
        set(v) {
            field = if (v == null || v >= this) v else throw RuntimeException("right child must have value greater than or equal to parent's")
        }
    var left: BinarySearchNode? = null
        set(v) {
            field = if (v == null || v < this) v else throw RuntimeException("left child must have value less than parent's")
        }

    val isRightChild get() = value >= parent!!.value
    val isLeftChild get() = value < parent!!.value

    val children get() = listOfNotNull(left, right)

    val sibling get() = when {
        isRightChild -> parent?.left
        isLeftChild -> parent?.right
        else -> null
    }

    val uncle get() = parent?.sibling

    val height: Int get() = 1 + (children.maxBy { it.height }?.value ?: 0)

    val maxNode: BinarySearchNode get() = if (right != null) right!!.maxNode else this

    val minNode: BinarySearchNode get() = if (left != null) left!!.minNode else this

    /**
     * predecessor is the next value in a reverse in-order list
     * max value of the left child (if it exists)
     * or
     * travel up the tree until you are the right child, and parent is the predecessor
     */
    val predecessor: BinarySearchNode? get() {
        if (left != null) return left?.maxNode

        var curNode = this
        var nextNode = parent

        while (nextNode != null && curNode.isLeftChild) {
            curNode = nextNode
            nextNode = curNode.parent
        }

        return nextNode
    }

    /**
     * successor is the next value in an in-order list
     * min value of the right child (if child exists)
     * or
     * travel up tree until you are the left child, and your parent is the successor
     */
    val successor: BinarySearchNode? get() {
        if (right != null) return right!!.minNode

        var curNode = this
        var nextNode = parent

        while (nextNode != null && curNode.isRightChild) {
            curNode = nextNode
            nextNode = curNode.parent
        }

        return nextNode
    }

    operator fun contains(value: Int): Boolean = contains(BinarySearchNode(value))
    operator fun contains(node: BinarySearchNode): Boolean = get(node.value) != null

    operator fun get(v: Int): BinarySearchNode? =
        if (v > value) right?.get(v) else if (v < value) left?.get(v) else this

    fun walk(order: WalkOrder = IN_ORDER): List<BinarySearchNode> = when(order) {
        IN_ORDER   -> listOf(left!!.walk(order), self, right!!.walk(order)).flatten()
        PRE_ORDER  -> listOf(self, left!!.walk(order), right!!.walk(order)).flatten()
        POST_ORDER -> listOf(left!!.walk(order), right!!.walk(order), self).flatten()
    }

    fun transplantWith(replacement: BinarySearchNode?) {
        if (isLeftChild) parent?.left = replacement
        if (isRightChild) parent?.right = replacement
        replacement?.parent = parent
    }

    // sets self to be garbage collected
    fun delete() {
        parent = null
        left = null
        right = null
    }

    operator fun compareTo(other: BinarySearchNode): Int = value.compareTo(other.value)

    fun copy(value: Int = this.value, parent: BinarySearchNode? = this.parent, right: BinarySearchNode? = this.right, left: BinarySearchNode? = this.left): BinarySearchNode {
        return BinarySearchNode(value, parent).apply {
            this.right = right
            this.left = left
        }
    }
}