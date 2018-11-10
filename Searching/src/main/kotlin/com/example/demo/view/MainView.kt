package com.example.demo.view

import com.example.demo.app.Styles
import com.example.demo.view.datastructures.DataStructuresView
import javafx.scene.control.Button
import tornadofx.*

class MainView : View("Algorithms Practice") {
    override val root = vbox {
        label(title) {
            addClass(Styles.heading)
        }
        label("Please select a Unit")
        button("III Data Structures") {
            action {
                replaceWith(DataStructuresView::class)
            }
        }
    }
}