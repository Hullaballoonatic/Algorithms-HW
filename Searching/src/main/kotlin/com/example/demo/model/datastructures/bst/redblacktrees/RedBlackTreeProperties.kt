package com.example.demo.model.datastructures.bst.redblacktrees

import com.example.demo.model.datastructures.bst.redblacktrees.Color.BLACK
import com.example.demo.model.datastructures.bst.redblacktrees.Color.RED

sealed class RedBlackTreeProperties(override val message: String) : Exception() {
    abstract fun check(tree: RedBlackTree<*>): Boolean

    object Property1 : RedBlackTreeProperties("All nodes must be colored RED or BLACK") {
        override fun check(tree: RedBlackTree<*>) = tree.asList().none { (it as RedBlackNode).color != RED || it.color != BLACK }
    }

    object Property2 : RedBlackTreeProperties("The root node must be colored BLACK") {
        override fun check(tree: RedBlackTree<*>) = tree.root?.color == BLACK
    }

    object Property3 : RedBlackTreeProperties("The leaf nodes must be colored BLACK") {
        override fun check(tree: RedBlackTree<*>) = true
    }

    object Property4 : RedBlackTreeProperties("If a node is RED, both its children must be BLACK") {
        override fun check(tree: RedBlackTree<*>) =
            tree.asList().filter { (it as RedBlackNode).color == RED }.map { it.children }.all { it == BLACK }
    }

    object Property5 : RedBlackTreeProperties("For any node, the number of BLACK nodes along the paths to each descendant leaf must be the same.") {
        override fun check(tree: RedBlackTree<*>) = try {
            tree.asList().forEach { (it as RedBlackNode).blackHeight }
            true
        } catch (_: Exception) {
            false
        }
    }
}

