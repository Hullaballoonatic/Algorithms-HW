package com.example.demo.view.datastructures.bst

import com.example.demo.model.datastructures.bst.BinarySearchNode
import com.example.demo.model.datastructures.bst.BinarySearchTree
import com.example.demo.view.MainView
import com.example.demo.view.datastructures.DataStructuresView
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

class BSTView : View("12 Binary Search Trees") {
    val bst = BinarySearchTree()

    override val root = borderpane {
        top = hbox {
            button("Back") {
                action {
                    replaceWith(DataStructuresView::class)
                }
            }
            label(title)
            button("Menu") {
                action {
                    replaceWith(MainView::class)
                }
            }
        }
        left = vbox {
            button("Add") {

            }
            button("Delete") {

            }
            separator()
            label("Print")
            button("Pre-Order") {

            }
            button("In-Order") {

            }
            button("Post-Order") {

            }
        }
        center {

        }
    }
}