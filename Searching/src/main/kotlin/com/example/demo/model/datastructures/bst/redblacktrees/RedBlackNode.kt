package com.example.demo.model.datastructures.bst.redblacktrees

import com.example.demo.model.datastructures.bst.WalkOrder
import com.example.demo.model.datastructures.bst.WalkOrder.*
import com.example.demo.model.datastructures.bst.redblacktrees.Color.BLACK
import com.example.demo.model.datastructures.bst.redblacktrees.RedBlackTreeProperties.*

class RedBlackNode(
    val value: Int,
    private val tree: RedBlackTree,
    var color: Color = BLACK,
    parent: RedBlackNode? = null
) {
    private val self = listOf(this)

    val violations: List<RedBlackTreeProperties> get() = listOf(Property1, Property2, Property3, Property4, Property5).filter { !it.check(tree) }

    val isRoot get() = parent == null

    var parent = parent
        set(v) {
            if (v != this) {
                alteredSinceLastCheck = true
                field = v
                if (isRoot) tree.root = this
                if (isLeftChild && v?.left != this) v?.left = this
                if (isRightChild && v?.right != this) v?.right = this
            }
        }

    var right: RedBlackNode? = null
        set(v) {
            if (v != this) {
                alteredSinceLastCheck = true
                field = if (v == null || v >= this) v else throw RuntimeException(
                    "right child must have value greater than or equal to parent's")
            }
        }

    var left: RedBlackNode? = null
        set(v) {
            if (v != this) {
                alteredSinceLastCheck = true
                field = if (v == null || v < this) v else throw RuntimeException(
                    "left child must have value less than parent's")
            }
        }

    val isRightChild get() = value >= parent?.value!!
    val isLeftChild get() = value < parent?.value!!

    val children get() = listOfNotNull(right, left)
    val uncle get() = parent?.sibling
    val grandparent get() = parent?.parent

    val sibling get() = when {
        isRightChild -> parent?.left
        isLeftChild  -> parent?.right
        else         -> null
    }

    /**
     * measures the height in only black nodes. leaf nodes are black
     */
    var alteredSinceLastCheck = true
    var height: Int = 0
        get() {
            if (!alteredSinceLastCheck) return field

            alteredSinceLastCheck = false

            val numLeft = left?.height ?: 1
            val numRight = right?.height ?: 1

            if (numLeft != numRight) throw Property5

            return numLeft + if (color == BLACK) 1 else 0
        }

    val maxNode: RedBlackNode get() = right?.maxNode ?: this

    val minNode: RedBlackNode get() = left?.minNode ?: this

    /**
     * predecessor is the next value in a reverse in-order list
     * max value of the left child (if it exists)
     * or
     * travel up the tree until you are the right child, and parent is the predecessor
     */
    val predecessor: RedBlackNode?
        get() {
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
    val successor: RedBlackNode?
        get() {
            if (right != null) return right!!.minNode

            var curNode = this
            var nextNode = parent

            while (nextNode != null && curNode.isRightChild) {
                curNode = nextNode
                nextNode = curNode.parent
            }

            return nextNode
        }

    /**
     *              /                             /
     *            y                             x
     *          /  \    <--Left Rotate-<      /  \
     *        x     c                       a     y
     *      /  \       >-Right Rotate-->        /  \
     *    a     b                             b     c
     */
    fun rotateLeft() {
        val inc = right
        right = inc?.left // inc's left subtree becomes right subtree
        inc?.parent = parent // reparent
        parent = inc
    }

    fun rotateRight() {
        val inc = left
        left = inc?.right
        inc?.parent = parent
        parent = inc
    }

    operator fun contains(value: Int): Boolean = get(value) != null
    operator fun contains(node: RedBlackNode): Boolean = get(node.value) != null

    operator fun get(v: Int): RedBlackNode? =
        if (v > value) right?.get(v) else if (v < value) left?.get(v) else this

    fun walk(order: WalkOrder = IN_ORDER): List<RedBlackNode> = when (order) {
        IN_ORDER   -> listOf(left!!.walk(order), self, right!!.walk(order)).flatten()
        PRE_ORDER  -> listOf(self, left!!.walk(order), right!!.walk(order)).flatten()
        POST_ORDER -> listOf(left!!.walk(order), right!!.walk(order), self).flatten()
    }

    fun transplantWith(replacement: RedBlackNode?) {
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

    operator fun compareTo(other: RedBlackNode): Int = value.compareTo(other.value)

    fun copy(
        value: Int = this.value,
        tree: RedBlackTree = this.tree,
        color: Color = this.color,
        parent: RedBlackNode? = this.parent,
        left: RedBlackNode? = this.left,
        right: RedBlackNode? = this.right
    ): RedBlackNode = RedBlackNode(value, tree, color, parent).apply {
        this.left = left
        this.right = right
    }
}