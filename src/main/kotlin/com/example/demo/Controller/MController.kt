package com.example.demo.Controller

import com.example.demo.model.Point
import com.example.demo.view.MainView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*
import java.awt.MouseInfo
import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MController : Controller() {
    fun main(){

    }
    val mainView: MainView by inject()

    var pathArrayToDraw = mutableListOf<Point>()

    var maxSpeed: Double = 0.0
    var prevX: Double = -1.0
    var prevY: Double = -1.0

    var labelCoords = SimpleStringProperty()
    var labelCurrSpeed = SimpleStringProperty("Current speed is: ")
    var labelMaxSpeed = SimpleStringProperty("Max speed is: ")

    var isProcessing: Boolean = false

    var filesList: ObservableList<String> = File(getDataPath(false)).list().toList().observable()


    fun updateFiles(){
        mainView.lv.items=File(getDataPath(false)).list().toList().observable()
    }

    fun executeValues() {
        var speed: Double
        var movementX: Double
        var movementY: Double
        var movement: Double

        runAsync {
            val currentPath = mutableListOf<Point>()

            while (isProcessing) {
                val x = MouseInfo.getPointerInfo().location.getX()
                val y = MouseInfo.getPointerInfo().location.getY()
                if (prevX == -1.0) {
                    prevX = x
                    prevY = y
                    continue
                }

                movementX = abs(x - prevX);
                movementY = abs(y - prevY);
                movement = sqrt(movementX * movementX + movementY * movementY)

                TimeUnit.MILLISECONDS.sleep(1)

                print("\n $x $y")

                currentPath.add(Point(x - 250.0, y))

                prevX = x
                prevY = y

                runLater { // ui calling
                    speed = movement //current speed
                    maxSpeed = round(if (speed > maxSpeed) speed.also { maxSpeed = it } else maxSpeed)
                    labelMaxSpeed.set("Max speed is: $maxSpeed")
                    labelCurrSpeed.set("Current speed is: ${round(speed)}")
                }
            }
            File("${getDataPath(false)}\\data${getNumberOfFiles() + 1}.txt")
                    .writeText(Gson().toJson(currentPath))
            ui{
                updateFiles()
            }
        }
    }

    fun drawCursorPath(file: String) {
        runAsync {
            println("\nReading from $file\n")
            val gson = Gson()
            val arrayType = object : TypeToken<MutableList<Point>>() {}.type
            val fileReader = FileReader(file)
            pathArrayToDraw = gson.fromJson(JsonReader(fileReader), arrayType)
            fileReader.close()
        }ui{
            val gc: GraphicsContext = mainView.cv.graphicsContext2D

            gc.fill = Color.FLORALWHITE
            gc.fillRect(0.0, 0.0, mainView.cv.width, mainView.cv.height)
            gc.lineWidth = 1.0
            gc.fill = Color.BLACK

//                        for(point in mController.pathArrayToDraw){
//                            runAsync{
//                                TimeUnit.NANOSECONDS.sleep(10000)
//                            } ui{
//                                gc.strokeOval(point.x, point.y, 1.0, 1.0)
//                                gc.stroke()
//                            }
//                    }

            var i = 0

            val drawPixel = Timeline(KeyFrame(Duration.millis(2.0), EventHandler<ActionEvent?> {
                val point = pathArrayToDraw[i]
                gc.strokeOval(point.x, point.y, 1.0, 1.0)
                i++
            }))
            drawPixel.cycleCount = pathArrayToDraw.size
            drawPixel.play()
        }

    }

    fun getNumberOfFiles(): Int {
        print("\nExisted files:\n")
        for (dataFile in File(getDataPath(false)).list()) {
            print("\n$dataFile")
        }

        return File(getDataPath(false)).list().size
    }

    fun getDataPath(isSrc: Boolean): String {
        return if (isSrc)
            "file:///${Paths.get("").toAbsolutePath()}"
        else
            "${Paths.get("").toAbsolutePath()}\\Data"
    }
}
