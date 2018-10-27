package model.dataStructures.bst

class BinarySearchNode(val value: Int, var parent: BinarySearchNode? = null) {
    private val self = this

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

    val sibling get() = when {
        isRightChild -> parent?.left
        isLeftChild -> parent?.right
        else -> null
    }

    val uncle get() = parent?.sibling

    val maxNode: BinarySearchNode get() {
        var curNode = right
        while (true) {
            if (curNode?.right == null) return curNode!!
            curNode = curNode.right
        }
    }

    val minNode: BinarySearchNode get() {
        var curNode = left
        while (true) {
            if (curNode?.left == null) return curNode!!
            curNode = curNode.left
        }
    }

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

    fun inOrderWalk(): ArrayList<BinarySearchNode> = ArrayList<BinarySearchNode>().apply {
        addAll(left!!.inOrderWalk())
        add(self)
        addAll(right!!.inOrderWalk())
    }

    fun preOrderWalk(): ArrayList<BinarySearchNode> = ArrayList<BinarySearchNode>().apply {
        add(self)
        addAll(left!!.preOrderWalk())
        addAll(right!!.preOrderWalk())
    }

    fun postOrderWalk(): ArrayList<BinarySearchNode> = ArrayList<BinarySearchNode>().apply {
        addAll(left!!.postOrderWalk())
        addAll(right!!.postOrderWalk())
        add(self)
    }

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
