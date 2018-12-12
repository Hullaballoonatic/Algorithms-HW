@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.example.demo.model.datastructures.bst.redblacktrees

import com.example.demo.model.datastructures.bst.BSTNode
import com.example.demo.model.datastructures.bst.redblacktrees.Color.BLACK
import com.example.demo.model.datastructures.bst.redblacktrees.RedBlackTreeProperties.*

class RedBlackNode<E: Comparable<E>>(
    value: E,
    var color: Color = BLACK,
    parent: RedBlackNode<E>? = null
) : BSTNode<E>(value, parent) {
    /**
     * retrieves the node with the provided order statistic
     * time complexity: O(lgn)
     */
    operator fun get(order: Int): RedBlackNode<E>? {
        return when {
            order == rank -> this
            order < rank  -> (left as RedBlackNode<E>?)?.get(order)
            else          -> (right as RedBlackNode<E>?)?.get(order)
        }
    }

    /**
     * measures the blackHeight in only black nodes. leaf nodes are black
     */
    var blackHeight: Int = 0
        get() {
            val numLeft = left?.height ?: 1
            val numRight = right?.height ?: 1

            if (numLeft != numRight) throw Property5

            return numLeft + if (color == BLACK) 1 else 0
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
        val replacement = right
        right = replacement?.left // inc's left subtree becomes right subtree
        replacement?.parent = parent // reparent
        parent = replacement
    }

    fun rotateRight() {
        val replacement = left
        left = replacement?.right
        replacement?.parent = parent
        parent = replacement
    }

    fun transplantWith(replacement: RedBlackNode<E>?) {
        if (isLeftChild) parent?.left = replacement
        if (isRightChild) parent?.right = replacement
        replacement?.parent = parent
    }

    fun copy(
        value: E = this.value,
        color: Color = this.color,
        parent: RedBlackNode<E>? = this.parent as RedBlackNode<E>,
        left: RedBlackNode<E>? = this.left as RedBlackNode<E>,
        right: RedBlackNode<E>? = this.right as RedBlackNode<E>
    ) : RedBlackNode<E> = RedBlackNode(value, color, parent).apply {
        this.left = left?.copy()
        this.right = right?.copy()
    }
}