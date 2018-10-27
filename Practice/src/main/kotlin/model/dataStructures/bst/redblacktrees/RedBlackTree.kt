package model.dataStructures.bst.redblacktrees

import model.dataStructures.bst.redblacktrees.Color.*

class RedBlackTree {
    var root: RedBlackNode? = null
        set(v) {
            field = v?.copy(parent = null, color = BLACK)
        }

    operator fun contains(value: Int) = get(value) != null

    operator fun get(value: Int, x: RedBlackNode? = root): RedBlackNode? = when {
        x == null        -> x
        x.value == value -> x
        x.value < value  -> get(value, x.left)
        else             -> get(value, x.right)
    }

    operator fun plusAssign(value: Int) {
        val newNode = RedBlackNode(value, RED)
    }

    operator fun plusAssign(node: RedBlackNode) {

    }

    operator fun minusAssign(value: Int) {

    }

    operator fun minusAssign(node: RedBlackNode) {

    }

    val max: Int? get() {
        var curNode = root
        while (true) {
            if (curNode?.left == null) return curNode?.value
            curNode = curNode.left
        }
    }

    val min: Int? get() {
        var curNode = root
        while (true) {
            if (curNode?.left == null) return curNode?.value
            curNode = curNode.left
        }
    }

    val isEmpty: Boolean = root == null
    val isNotEmpty: Boolean = root != null
    val height: Int get() { TODO() }
}