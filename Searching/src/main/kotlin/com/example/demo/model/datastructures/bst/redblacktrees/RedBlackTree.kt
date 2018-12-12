@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.example.demo.model.datastructures.bst.redblacktrees

import com.example.demo.model.datastructures.bst.WalkOrder
import com.example.demo.model.datastructures.bst.WalkOrder.IN_ORDER
import com.example.demo.model.datastructures.bst.redblacktrees.Color.BLACK
import com.example.demo.model.datastructures.bst.redblacktrees.Color.RED
import com.example.demo.model.datastructures.bst.redblacktrees.RedBlackTreeProperties.*

class RedBlackTree<E : Comparable<E>>(root: RedBlackNode<E>? = null) {
    var root = root
        set(v) {
            field = v
            v?.color = BLACK
        }

    fun orderStatisticRank(node: RedBlackNode<E>): Int {
        var r = node.rank
        var y = node
        while (y != root) {
            if (y.isRightChild)
                r += y.rank
            y = y.parent as RedBlackNode<E>
        }
        return r
    }

    operator fun get(order: Int): RedBlackNode<E>? = root?.get(order)

    val height: Int get() = root?.blackHeight ?: 0

    val max get() = root?.max

    val min get() = root?.min

    val isEmpty: Boolean = root == null

    fun asList(order: WalkOrder = IN_ORDER) = root?.walk(order) ?: emptyList()

    operator fun contains(v: E) = get(v) != null

    operator fun get(v: E): RedBlackNode<E>? = root?.get(v) as RedBlackNode<E>?

    val violations: List<RedBlackTreeProperties>
        get() = listOf(Property1, Property2, Property3, Property4, Property5).filter { !it.check(this) }

    /**
     * Insert
     * Do from BST insert then fixUp things
     */
    operator fun plusAssign(v: E) = plusAssign(RedBlackNode(v, RED))

    operator fun plusAssign(node: RedBlackNode<E>) {
        var pre: RedBlackNode<E>? = null
        var cur = root

        while (cur != null) {
            pre = cur
            cur = (if (node.value < cur.value) cur.left else cur.right) as RedBlackNode<E>?
        }
        node.parent = pre
        if (node.parent == null)
            root = node
        insertFix(node)
    }

    /**
     * Delete
     * do from bst delete while maintaining the node that is removed or moved as well as its color
     * then fixUp things
     */
    operator fun minusAssign(v: E) = minusAssign(get(v) ?: throw Exception("v does not exist in tree"))

    operator fun minusAssign(node: RedBlackNode<E>) {
        var xfer: RedBlackNode<E>? = null
        var doFix = xfer?.color == BLACK
        when (node.children.size) {
            1 -> {
                xfer = node.children.first() as RedBlackNode<E>?
                node.transplantWith(node.children.first() as RedBlackNode<E>?)

                if (node.isRoot) root = node.children.first() as RedBlackNode<E>?
            }
            2 -> {
                val replacement = node.successor as RedBlackNode<E>?

                doFix = replacement!!.color == BLACK

                xfer = replacement.right as RedBlackNode<E>?

                if (replacement == node.right) {
                    xfer?.parent = replacement
                } else {
                    replacement.transplantWith(replacement.right as RedBlackNode<E>?)
                    replacement.right = node.right
                    replacement.right?.parent = replacement
                }
                node.transplantWith(replacement)
                replacement.left = node.left
                replacement.left?.parent = node
                replacement.color = node.color

                if (node.isRoot) root = replacement
            }
        }
        node.delete()
        if (doFix) deleteFix(xfer!!)
    }

    /**
     * insert can only screw up properties 2 and 4
     * 2 is handled at the end
     * 4 is handled in 3 cases:
     *  invariant: cur is always red. cur's grandparent is always black.
     *            we must keep doing operations while cur's parent is red, because that violates 4
     *            when cur's parent is black, we're done
     *
     *      case 1 : uncle is red ->
     *          recolor
     *          cur becomes grandparent
     *
     *      case 2 : uncle is black and cur is opposite L/R child as parent ->
     *          cur becomes parent
     *          rotate on parent in direction away from uncle
     *          ALSO
     *
     *      case 3 : uncle is black and cur is same L/R child as parent ->
     *          recolor
     *          rotate on grandparent in direction of uncle
     *          no change to cur
     *
     * note: recoloring always involves parent and uncle become black, grandparent become red
     */
    private fun insertFix(node: RedBlackNode<E>) {
        var inserted = node
        val iParent = inserted.parent as RedBlackNode<E>?
        val iUncle = inserted.uncle as RedBlackNode<E>?
        val iGrandparent = inserted.grandparent as RedBlackNode<E>?
        while (iParent?.color == RED) {
            when {
                iUncle!!.color == RED -> {
                    iUncle.color = BLACK
                    iParent.color = BLACK
                    iGrandparent!!.color = RED
                    inserted = inserted.grandparent!! as RedBlackNode<E>
                }
                iParent.isLeftChild   -> {
                    if (inserted.isRightChild) {
                        inserted = inserted.parent!! as RedBlackNode<E>
                        inserted.rotateLeft()
                    }
                    iUncle.color = BLACK
                    iParent.color = BLACK
                    iGrandparent!!.color = RED
                    iGrandparent.rotateLeft()
                }
                else                  -> {
                    if (inserted.isLeftChild) {
                        inserted = inserted.parent!! as RedBlackNode<E>
                        inserted.rotateRight()
                    }
                    iUncle.color = BLACK
                    iParent.color = BLACK
                    iGrandparent!!.color = RED
                    iGrandparent.rotateRight()
                }
            }
        }
        root!!.color = BLACK
    }

    /**
     * only case 2 causes the fixUp to repeat
     */
    private fun deleteFix(node: RedBlackNode<E>) {
        var transferred = node
        val tSibling = transferred.sibling as RedBlackNode<E>?
        val tParent = transferred.parent as RedBlackNode<E>?
        val tUncle = transferred.uncle as RedBlackNode<E>?
        while (!transferred.isRoot && transferred.color == BLACK) {
            if (tSibling!!.color == RED) {
                tSibling.color = BLACK                             // case 1
                tParent!!.color = RED                                // case 1
                tParent.rotateLeft()                               // case 1
            }
            if (tUncle!!.children.all { (it as RedBlackNode<E>).color == BLACK }) {
                tUncle.color = RED                                 // case 2
                transferred = transferred.parent!! as RedBlackNode<E>                             // case 2
            } else {
                if (transferred.isLeftChild) {
                    if ((tSibling.right as RedBlackNode<E>?)?.color == BLACK) {
                        (tSibling.left as RedBlackNode<E>?)?.color = BLACK               // case 3
                        tSibling.color = RED                       // case 3
                        tSibling.rotateRight()                     // case 3
                    }
                    tSibling.color = tParent!!.color    // case 4
                    tParent.color = BLACK                          // case 4
                    (tSibling.right!! as RedBlackNode<E>).color = BLACK                 // case 4
                    tParent.rotateLeft()
                } else {
                    if ((tSibling.left as RedBlackNode<E>?)?.color == BLACK) {
                        (tSibling.right as RedBlackNode<E>?)?.color = BLACK              // case 3
                        tSibling.color = RED                       // case 3
                        tSibling.rotateLeft()                      // case 3
                    }
                    tSibling.color = tParent!!.color    // case 4
                    tParent.color = BLACK                          // case 4
                    (tSibling.left!! as RedBlackNode<E>).color = BLACK                  // case 4
                    tParent.rotateRight()                          // case 4
                }
            }
        }
        root!!.color = BLACK
    }

    fun splitTree(root: RedBlackNode<E>): Pair<RedBlackTree<E>, RedBlackTree<E>> {
        val newRoot = get(root.value)
        if (newRoot != null)
            if (newRoot.isLeftChild) newRoot.parent!!.left = null
            else newRoot.parent!!.right = null
        return this to RedBlackTree(newRoot)
    }
}