package com.example.demo.view

import com.example.demo.MController
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritablePixelFormat
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*
import java.nio.ByteBuffer


class MainView : View() {
    private val mController: MController by inject()
    var cv = Canvas()

    lateinit var v1: VBox

    override val root = hbox {
        v1 = vbox {
            spacing = 10.0
            minWidth = 250.0
            maxWidth = 250.0
            style {
                alignment = Pos.BASELINE_CENTER
                vboxConstraints {}
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
            label(mController.labelAcceleration)
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
                    val gc: GraphicsContext = cv.graphicsContext2D

                    gc.fill = Color.FLORALWHITE
                    gc.fillRect(0.0, 0.0, cv.width, cv.height)
                    gc.lineWidth=1.0
                    gc.fill=Color.BLACK
                    gc.moveTo(mController.pathArrayToDraw[0].x, mController.pathArrayToDraw[0].y)

                    for(point in mController.pathArrayToDraw){
                        gc.lineTo(point.x, point.y)
                        gc.stroke()
                    }
                }
            }
        }
        cv = canvas {
            hgrow = Priority.ALWAYS
            maxWidth = Double.MAX_VALUE
            style {
                alignment = Pos.BASELINE_RIGHT
                borderColor += box(
                        top = Color.RED,
                        right = Color.DARKGREEN,
                        left = Color.ORANGE,
                        bottom = Color.PURPLE
                )
            }
            width = 1350.0
            height = 800.0
        }
    }

}