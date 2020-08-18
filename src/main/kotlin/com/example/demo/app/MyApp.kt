package com.example.demo.app

import com.example.demo.controller.MController
import com.example.demo.view.MainView
import javafx.geometry.Rectangle2D
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File
import java.nio.file.Paths

class MyApp : App(MainView::class) {

    fun main(args: Array<String>) {
        launch<MyApp>(args)
    }

//    override val primaryView = MainView::class

    init {
        importStylesheet(Styles::class)
    }

    private val mController: MController by inject()
    private val iconURL = mController.getDataPath(true) + "/src/main/kotlin/com/example/demo/app/icon.png"
    override fun start(stage: Stage) {
        var primScreenBounds = Rectangle2D(0.0, 0.0, 1920.0, 1280.0)

        runAsync {
            primScreenBounds = Screen.getPrimary().visualBounds
        }
        stage.initStyle(StageStyle.DECORATED)
        stage.isMaximized = true
        with(stage) {
            isResizable = false
            width = (primScreenBounds.width).also {
                print("$it\n")
            }
            height = (primScreenBounds.height).also {
                print("$it\n")
            }
            icons.add(Image(iconURL))
        }
        super.start(stage)
    }
}