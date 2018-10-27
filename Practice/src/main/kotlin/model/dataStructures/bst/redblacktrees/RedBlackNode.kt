package model.dataStructures.bst.redblacktrees

import model.dataStructures.bst.redblacktrees.Color.BLACK

class RedBlackNode(
    val value:  Int,
    var color:  Color = BLACK,
    var parent: RedBlackNode? = null
) {
    var right: RedBlackNode? = null
        set(v) {
            field = if (v == null || v >= this) v else throw RuntimeException("right child must have value greater than or equal to parent's")
        }
    var left: RedBlackNode? = null
        set(v) {
            field = if (v == null || v < this) v else throw RuntimeException("left child must have value less than parent's")
        }

    val isRightChild get() = value >= parent!!.value
    val isLeftChild get() = value < parent!!.value

    val sibling get() = when {
        isRightChild -> parent!!.left
        isLeftChild -> parent!!.right
        else -> null
    }

    val uncle get() = parent?.sibling

    operator fun compareTo(other: RedBlackNode): Int = value.compareTo(other.value)

    fun copy(value: Int = this.value, color: Color = this.color, parent: RedBlackNode? = this.parent as RedBlackNode?, left: RedBlackNode? = this.left as RedBlackNode?, right: RedBlackNode? = this.right as RedBlackNode?): RedBlackNode {
        return RedBlackNode(value, color, parent).apply {
            this.left = left
            this.right = right
        }
    }
}

