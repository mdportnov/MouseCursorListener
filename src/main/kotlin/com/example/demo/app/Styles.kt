package com.example.demo.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val labelColor = c("#a94442")
    }

    init {
        root{
            label {
                padding = box(10.px)
                backgroundColor += Color.WHITESMOKE
                fontSize = 20.px
                fontWeight = FontWeight.BOLD
            }
        }
    }
}