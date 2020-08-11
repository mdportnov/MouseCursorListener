package com.example.demo.view

import com.example.demo.Controller.MController
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ListView
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*


class MainView : View() {
    private val mController: MController by inject()
    var cv = Canvas()

    lateinit var v1: VBox
    var lv: ListView<String> = ListView()
    lateinit var selModel: MultipleSelectionModel<String>

    override val root = hbox {
        v1 = vbox {
            spacing = 10.0
            minWidth = 250.0
            maxWidth = 250.0
            maxHeight = Double.MAX_VALUE
            style {
                alignment = Pos.BASELINE_CENTER
                borderColor += box(
                        top = Color.RED,
                        right = Color.DARKGREEN,
                        left = Color.ORANGE,
                        bottom = Color.PURPLE
                )
            }
            label(mController.labelCoords)
            label(mController.labelCurrSpeed)
            label(mController.labelMaxSpeed)
            button {
                this.text = "Clear max speed"
                action {
                    mController.labelMaxSpeed.set("Max speed is: ")
                }
            }
            button {
                text = "Start logging"
                action {
                    mController.isProcessing = true
                    mController.executeValues()
                }
            }
            button {
                text = "Stop logging"
                action {
                    mController.isProcessing = false
                    mController.updateFiles()
                }
            }
            button {
                text = "Clear canvas"
                action {
                    val gc: GraphicsContext = cv.graphicsContext2D
                    gc.fill = Color.FLORALWHITE
                    gc.fillRect(0.0, 0.0, cv.width, cv.height)
                }
            }
            button {
                text = "Draw last path"
                action {
                    mController.isProcessing = false
                    mController.drawCursorPath("${mController.getDataPath(false)}\\data${mController.getNumberOfFiles()}.txt")
                }
            }
            scrollpane {
                vboxConstraints {
                    marginTop = 200.0
                }
                prefViewportHeight = 300.0
                lv = listview(mController.filesList) {
                    minHeight = 400.0
                    maxHeight = 600.0
                    prefHeight = 600.0
                    style {
                        borderColor += box(Color.BLACK)
                        selectionModel.selectionMode = SelectionMode.MULTIPLE
                    }
                    cellFormat {
                        text = it.toString()
                    }
                    selModel = lv.selectionModel

                    selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
                        if (newValue != null) {
                            mController.drawCursorPath("${mController.getDataPath(false)}\\$newValue")
                        } else return@addListener
                    }
//                    bindSelected(mController.selectedFiles)

                }
            }
        }
        stackpane {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            maxWidth = Double.MAX_VALUE
            style {
                backgroundColor += c(Color.FLORALWHITE.toString())
                alignment = Pos.BASELINE_RIGHT
            }
            anchorpane {
                cv = canvas {
                    style {
                        height = primaryStage.height - v1.height
                        width = primaryStage.width - v1.width
                        borderColor += box(
                                top = Color.RED,
                                right = Color.DARKGREEN,
                                left = Color.ORANGE,
                                bottom = Color.PURPLE
                        )
                    }
                }
                stackpane {
                    minHeight = primaryStage.height - v1.height
                    minWidth = primaryStage.width - 257.0
                    style {
                        borderColor += box(
                                top = Color.BLACK,
                                right = Color.BLACK,
                                left = Color.BLACK,
                                bottom = Color.BLACK
                        )
                    }
                    var b1 = button {
                        id = "b1"
                        text = "start"
                        alignment = Pos.TOP_RIGHT


                        StackPane.setAlignment(this, Pos.BOTTOM_LEFT)
                        StackPane.setMargin(this, Insets(0.0, 0.0, 50.0, 50.0))
                    }
                    var b2 = button {
                        id = "b2"
                        text = "stop"
                        alignment = Pos.TOP_RIGHT
                        StackPane.setAlignment(this, Pos.TOP_RIGHT)
                        StackPane.setMargin(this, Insets(50.0, 50.0, 0.0, 0.0))

                    }
                }

            }
        }
    }
}