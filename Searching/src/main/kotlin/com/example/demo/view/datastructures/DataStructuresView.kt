package com.example.demo.view.datastructures

import com.example.demo.view.MainView
import com.example.demo.view.datastructures.bst.BSTView
import com.example.demo.view.datastructures.bst.RBTView
import tornadofx.*

class DataStructuresView : View("III Data Structures") {
    override val root = borderpane {
        top = hbox {
            button("back") {
                action {
                    replaceWith(MainView::class)
                }
            }
            label(title)
            button("menu") {
                action {
                    replaceWith(MainView::class)
                }
            }
        }
        center = vbox {
            label("Please select from chapter")
            button("12 Binary Search Trees") {
                action {
                    replaceWith(BSTView::class)
                }
            }
            button("13 Red-Black Trees") {
                action {
                    replaceWith(RBTView::class)
                }
            }
        }
    }
}