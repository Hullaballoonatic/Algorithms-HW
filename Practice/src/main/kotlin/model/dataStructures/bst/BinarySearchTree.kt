package model.dataStructures.bst

import kotlin.math.max

class BinarySearchTree {
    var root: BinarySearchNode? = null
        set(v) {
            field = v?.copy(parent = null)
        }

    operator fun contains(value: Int) = get(value) != null

    operator fun get(value: Int, x: BinarySearchNode? = root): BinarySearchNode? = when {
        x == null        -> x
        x.value == value -> x
        x.value < value  -> get(value, x.left)
        else             -> get(value, x.right)
    }

    operator fun plusAssign(value: Int) = plusAssign(BinarySearchNode(value))
    operator fun plusAssign(node: BinarySearchNode) {
        var generation = 1
        var previousNode = root
        var curNode = root

        while(curNode != null) {
            generation++
            previousNode = curNode
            curNode = if (node < curNode) curNode.left else curNode.right
        }
        node.parent = previousNode

        height = max(generation, height)

        when {
            previousNode == null -> root = node
            previousNode <= node -> previousNode.right = node
            else                 -> previousNode.left = node
        }
    }

    operator fun minusAssign(value: Int) = minusAssign(BinarySearchNode(value))

    operator fun minusAssign(node: BinarySearchNode) {
        when {
            node.left  == null -> transplant(node, node.right)
            node.right == null -> transplant(node, node.left)
            else -> {
                val replacement = node.minNode
                if (node.minNode != node) {
                    transplant(replacement, replacement.right)
                    replacement.right = node.right
                    replacement.right?.parent = replacement
                }
                transplant(node, replacement)
                replacement.left = node.left
                replacement.left?.parent = replacement
            }
        }

        node.delete()
    }

    private fun transplant(removed: BinarySearchNode, replacement: BinarySearchNode?) {
        when {
            removed.parent == null -> root = replacement
            removed.isLeftChild    -> removed.parent?.left = replacement
            else                   -> removed.parent?.right = replacement
        }
        if(replacement != null) replacement.parent = removed.parent
    }

    val sortedInOrder: List<BinarySearchNode> get() = root?.inOrderWalk() ?: emptyList()

    var height: Int = 0

    val maxNode: BinarySearchNode? get() = root?.maxNode

    val minNode: BinarySearchNode? get() = root?.minNode

    val max get() = maxNode?.value

    val min get() = minNode?.value

    val isEmpty: Boolean = root == null
    val isNotEmpty: Boolean = root != null
}