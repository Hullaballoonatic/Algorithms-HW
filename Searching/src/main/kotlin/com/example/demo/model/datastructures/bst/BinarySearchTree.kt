package com.example.demo.model.datastructures.bst

import com.example.demo.model.datastructures.bst.WalkOrder.IN_ORDER
import java.lang.Exception

class BinarySearchTree(root: BinarySearchNode? = null) {
    var root: BinarySearchNode? = root
        set(v) {
            field = v?.copy(parent = null)
        }

    operator fun contains(value: Int) = get(value) != null

    operator fun get(value: Int, r: BinarySearchNode? = root) = r?.get(value)

    /**
     * Insert
     * travel from root.
     * if value is larger than current node, go right, otherwise go left
     * when you are a leaf node, insert value here
     */
    operator fun plusAssign(value: Int) = plusAssign(BinarySearchNode(value))
    operator fun plusAssign(node: BinarySearchNode) {
        var previousNode = root
        var curNode = root

        while(curNode != null) {
            previousNode = curNode
            curNode = if (node < curNode) curNode.left else curNode.right
        }

        node.parent = previousNode
    }

    /**
     * deletion occurs in three cases based on number of children:
     * 0 : delete yourself
     * 1 : transplantWith yourself with your child
     * 2 : put successor in your place, then delete the successor
     *
     * example of most complex replacement (z replaced)
     * replace z w/ successor y.    split with y move          merge.
     *                q                  q                      q
     *              /                  /                      /
     *            z                  z  ... y               y
     *          /  \               /  \      \            /  \
     *        /     \            /            \         /     \
     *      l        r         l               r      l        r
     *    /  \     /  \      /  \            /  \   /  \     /  \
     *           y                         /               /
     *            \                      x               x
     *             x                   /  \            /  \
     *           /  \
     */
    operator fun minusAssign(value: Int) = minusAssign(BinarySearchNode(value))
    operator fun minusAssign(node: BinarySearchNode) {
        when (node.children.size) {
            0    -> { }
            1    -> node.transplantWith(node.children.first())
            2    -> {
                val replacement = node.successor
                if (replacement != node.right) {
                    replacement?.transplantWith(replacement.right)
                    replacement?.right = node.right
                    replacement?.right?.parent = replacement
                }
                node.transplantWith(replacement)
                replacement?.left = node.left
                replacement?.left?.parent = node
            }
            else -> throw Exception("Too many children for Red-Black Tree")
        }

        node.delete()
    }

    fun splitTree(root: BinarySearchNode): Pair<BinarySearchTree, BinarySearchTree> {
        val newRoot = get(root.value)
        if (newRoot != null)
            if (newRoot.isLeftChild) newRoot.parent!!.left = null
            else newRoot.parent!!.right = null
        return this to BinarySearchTree(newRoot)
    }

    fun asList(order: WalkOrder = IN_ORDER) = root?.walk(order) ?: emptyList()

    val height get() = root?.height ?: 0

    val maxNode get() = root?.maxNode
    val max get() = maxNode?.value

    val minNode get() = root?.minNode
    val min get() = minNode?.value

    val isEmpty: Boolean = root == null
    val isNotEmpty: Boolean = root != null
}