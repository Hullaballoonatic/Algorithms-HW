@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.example.demo.model.datastructures.bst

import com.example.demo.model.datastructures.bst.WalkOrder.IN_ORDER
import kotlin.Exception

class BinarySearchTree<E: Comparable<E>>(root: BSTNode<E>? = null) {
    var root: BSTNode<E>? = root
        set(v) {
            field = v?.copy(parent = null)
        }

    operator fun contains(v: E) = root?.contains(v) ?: false

    operator fun get(v: E) = root?.get(v)

    /**
     * Insert
     * travel from root.
     * if value is larger than current node, go right, otherwise go left
     * when you are from leaf node, insert value here
     */
    operator fun plusAssign(v: E) = plusAssign(BSTNode(v))
    operator fun plusAssign(node: BSTNode<E>) {
        if (root == null) {
            root = node
        } else {
            var pre = root
            var cur = root

            while (cur != null) {
                pre = cur
                cur = if (node.value < cur.value) cur.left else cur.right
            }

            node.parent = pre
        }
    }
    operator fun plusAssign(v: Collection<E>) = v.forEach { plusAssign(it) }

    /**
     * deletion occurs in three cases based on number of children:
     * 0 : delete yourself
     * 1 : transplantWith yourself with your child
     * 2 : put successor in your place, then delete the successor
     *
     * example of most complex replacement (z replaced)
     * replace z w/ successor y.    split with y move              merge.
     *                q                  q                          q
     *              /                  /                          /
     *            z                  z  ... y                   y
     *          /  \               /  \      \                /  \
     *        /     \            /            \             /     \
     *      l        r         l               r          l        r
     *    /  \     /  \      /  \            /  \       /  \     /  \
     *           y                         /                   /
     *            \                      x                   x
     *             x                   /  \                /  \
     *           /  \
     */
    operator fun minusAssign(v: E) {
        val node = get(v) ?: throw Exception("tree does not contain value")
        when (node.children.size) {
            0    -> { // has no children / is from leaf node : just remove it
            }
            1    -> { // has only one child : replace yourself with it
                val replacement = node.children.first()
                replacement.parent = node.parent

                if (node.isRoot) root = replacement
            }
            2    -> {
                val replacement = node.successor
                if (replacement != node.right) replacement?.right = node.right

                replacement?.left = node.left
                replacement?.parent = node.parent

                if (node.isRoot) root = replacement
            }
            else -> throw Exception("Too many children for Red-Black Tree")
        }

        node.delete()
    }
    operator fun minusAssign(node: BSTNode<E>) = minusAssign(node.value)

    fun splitTree(root: BSTNode<E>): Pair<BinarySearchTree<E>, BinarySearchTree<E>> {
        val newRoot = get(root.value)
        if (newRoot != null)
            if (newRoot.isLeftChild) newRoot.parent!!.left = null
            else newRoot.parent!!.right = null
        return this to BinarySearchTree(newRoot)
    }

    fun asList(order: WalkOrder = IN_ORDER) = root?.walk(order) ?: emptyList()

    val height get() = root?.height ?: 0

    val max get() = root?.max

    val min get() = root?.min

    val isEmpty: Boolean = root == null
    val isNotEmpty: Boolean = root != null

    companion object {
        operator fun <E: Comparable<E>> invoke(c: Collection<E>) = BinarySearchTree<E>().apply { plusAssign(c) }
    }
}

fun <E: Comparable<E>> Collection<E>.toBST() = BinarySearchTree(this)