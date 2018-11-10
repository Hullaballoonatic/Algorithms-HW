package com.example.demo.model.datastructures.bst.redblacktrees

import com.example.demo.model.datastructures.bst.WalkOrder
import com.example.demo.model.datastructures.bst.WalkOrder.IN_ORDER
import com.example.demo.model.datastructures.bst.redblacktrees.Color.*

class RedBlackTree(var root: RedBlackNode? = null) {
    val height: Int get() = root?.height ?: 0

    val maxNode get() = root?.maxNode
    val max: Int? get() = maxNode?.value

    val minNode get() = root?.minNode
    val min: Int? get() = minNode?.value

    val isEmpty: Boolean = root == null

    fun asList(order: WalkOrder = IN_ORDER) = root?.walk(order) ?: emptyList()

    operator fun contains(value: Int) = get(value) != null

    operator fun get(value: Int, r: RedBlackNode? = root) = r?.get(value)

    /**
     * Insert
     * Do a BST insert then fix things
     */
    operator fun plusAssign(value: Int) = plusAssign(RedBlackNode(value, this, RED))
    operator fun plusAssign(node: RedBlackNode) {
        if (node.color != RED) throw Exception("Inserted Nodes must always be RED")

        var previousNode = root
        var curNode = root

        while(curNode != null) {
            previousNode = curNode
            curNode = if (node < curNode) curNode.left else curNode.right
        }

        node.parent = previousNode
        insertFix(node)
    }

    /**
     * Delete
     * do a bst delete while maintaining the node that is removed or moved as well as its color
     * then fix things
     */
    operator fun minusAssign(value: Int) = minusAssign(get(value) ?: throw Exception("value does not exist in tree"))
    operator fun minusAssign(node: RedBlackNode) {
        var transfer: RedBlackNode? = null
        var doFix = transfer?.color == BLACK
        when (node.children.size) {
            1 -> {
                transfer = node.children.first()
                node.transplantWith(node.children.first())
            }
            2 -> {
                val replacement = node.successor

                if (replacement!!.color == BLACK) {
                    doFix = true
                }

                transfer = replacement.right

                if (replacement == node.right) {
                    transfer?.parent = replacement
                } else {
                    replacement.transplantWith(replacement.right)
                    replacement.right = node.right
                    replacement.right?.parent = replacement
                }
                node.transplantWith(replacement)
                replacement.left = node.left
                replacement.left?.parent = node
                replacement.color = node.color
            }
        }
        node.delete()
        if (doFix) deleteFix(transfer!!)
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
    private fun insertFix(node: RedBlackNode) {
        var inserted = node
        while (inserted.parent?.color == RED) {
            when {
                inserted.uncle!!.color == RED -> {
                    inserted.uncle!!.color = BLACK
                    inserted.parent!!.color = BLACK
                    inserted.grandparent!!.color = RED
                    inserted = inserted.grandparent!!
                }
                inserted.parent!!.isLeftChild -> {
                    if (inserted.isRightChild) {
                        inserted = inserted.parent!!
                        inserted.rotateLeft()
                    }
                    inserted.uncle!!.color = BLACK
                    inserted.parent!!.color = BLACK
                    inserted.grandparent!!.color = RED
                    inserted.grandparent!!.rotateLeft()
                }
                else                          -> {
                    if (inserted.isLeftChild) {
                        inserted = inserted.parent!!
                        inserted.rotateRight()
                    }
                    inserted.uncle!!.color = BLACK
                    inserted.parent!!.color = BLACK
                    inserted.grandparent!!.color = RED
                    inserted.grandparent!!.rotateRight()
                }
            }
        }
        root!!.color = BLACK
    }

    /**
     * only case 2 causes the fix to repeat
     */
    private fun deleteFix(node: RedBlackNode) {
        var transferred = node
        while (!transferred.isRoot && transferred.color == BLACK) {
            if (transferred.sibling!!.color == RED) {
                transferred.sibling!!.color = BLACK                             // case 1
                transferred.parent!!.color = RED                                // case 1
                transferred.parent!!.rotateLeft()                               // case 1
            }
            if (transferred.uncle!!.children.all { it.color == BLACK }) {
                transferred.uncle!!.color = RED                                 // case 2
                transferred = transferred.parent!!                              // case 2
            } else {
                if (transferred.isLeftChild) {
                    if (transferred.sibling!!.right?.color == BLACK) {
                        transferred.sibling!!.left?.color = BLACK               // case 3
                        transferred.sibling!!.color = RED                       // case 3
                        transferred.sibling!!.rotateRight()                     // case 3
                    }
                    transferred.sibling!!.color = transferred.parent!!.color    // case 4
                    transferred.parent!!.color = BLACK                          // case 4
                    transferred.sibling!!.right!!.color = BLACK                 // case 4
                    transferred.parent!!.rotateLeft()
                } else {
                    if (transferred.sibling!!.left?.color == BLACK) {
                        transferred.sibling!!.right?.color = BLACK              // case 3
                        transferred.sibling!!.color = RED                       // case 3
                        transferred.sibling!!.rotateLeft()                      // case 3
                    }
                    transferred.sibling!!.color = transferred.parent!!.color    // case 4
                    transferred.parent!!.color = BLACK                          // case 4
                    transferred.sibling!!.left!!.color = BLACK                  // case 4
                    transferred.parent!!.rotateRight()                          // case 4
                }
            }
        }
        root!!.color = BLACK
    }

    fun splitTree(root: RedBlackNode): Pair<RedBlackTree, RedBlackTree> {
        val newRoot = get(root.value)
        if (newRoot != null)
            if (newRoot.isLeftChild) newRoot.parent!!.left = null
            else newRoot.parent!!.right = null
        return this to RedBlackTree(newRoot)
    }
}