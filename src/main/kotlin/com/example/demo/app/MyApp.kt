package com.example.demo.app

import com.example.demo.MController
import com.example.demo.view.MainView
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*

class MyApp : App(MainView::class, Styles::class) {
    private val mController: MController by inject()

    private val iconURL = mController.getDataPath(true) + "/src/main/kotlin/com/example/demo/app/icon.png"
    override fun start(stage: Stage) {
        with(stage) {
            width = 1600.0
            height = 800.0
            icons.add(Image(iconURL))
        }
        super.start(stage)
    }
}