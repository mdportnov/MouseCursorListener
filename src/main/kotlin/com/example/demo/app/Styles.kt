package com.example.demo.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        reloadStylesheetsOnFocus()
        importStylesheet(" file:///C:\\Users\\Mi\\IdeaProjects\\mouseListenerApp\\src\\main\\kotlin\\com\\example\\demo\\resources\\main.css")

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