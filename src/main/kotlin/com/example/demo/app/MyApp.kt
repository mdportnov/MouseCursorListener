package com.example.demo.app

import com.example.demo.Controller.MController
import com.example.demo.view.MainView
import javafx.geometry.Rectangle2D
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

class MyApp : App(MainView::class, Styles::class) {
    private val mController: MController by inject()
    var primScreenBounds: Rectangle2D = Screen.getPrimary().visualBounds

    private val iconURL = mController.getDataPath(true) + "/src/main/kotlin/com/example/demo/app/icon.png"
    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.DECORATED)
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