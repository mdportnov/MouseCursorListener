package com.example.demo

import com.example.demo.model.Point
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import javafx.beans.property.SimpleStringProperty
import javafx.scene.canvas.Canvas
import tornadofx.*
import java.awt.MouseInfo
import java.io.File
import java.io.FileReader
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MController : Controller() {
    var pathArrayToDraw = mutableListOf<Point>()

    var maxSpeed: Double = 0.0
    var prevSpeed: Double = 0.0
    var prevX: Double = -1.0
    var prevY: Double = -1.0

    var labelCoords = SimpleStringProperty()
    var labelCurrSpeed = SimpleStringProperty("Current speed is: ")
    var labelMaxSpeed = SimpleStringProperty("Max speed is: ")
    var labelAcceleration = SimpleStringProperty("Current acceleration is: ")

    var isProcessing: Boolean = false

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

                TimeUnit.MILLISECONDS.sleep(10)

                print("\n $x $y")

                currentPath.add(Point(x, y))

                prevX = x
                prevY = y

                runLater { // ui calling
                    speed = movement //current speed
                    maxSpeed = round(if (speed > maxSpeed) speed.also { maxSpeed = it } else maxSpeed)
                    labelMaxSpeed.set("Max speed is: $maxSpeed")
                    labelAcceleration.set("Current acceleration is: ${round(10 * (speed - prevSpeed))}")
                    labelCurrSpeed.set("Current speed is: ${round(speed)}")
                }
            }
            File("${getDataPath(false)}\\data${getNumberOfFiles()+1}.txt")
                    .writeText(Gson().toJson(currentPath))
        }
    }

    fun drawCursorPath(file: String) {
        println("\nReading from $file\n")
        val gson = Gson()
        val arrayType = object : TypeToken<MutableList<Point>>() {}.type
        pathArrayToDraw = gson.fromJson(JsonReader(FileReader(file)), arrayType)
    }

    fun getDataPath(isSrc: Boolean): String {
        return if(isSrc)
            "file:///${Paths.get("").toAbsolutePath()}"
        else
            "${Paths.get("").toAbsolutePath()}\\Data"

    }

    fun getNumberOfFiles(): Int {
        print(getDataPath(false))
        for(dataFile in File(getDataPath(false)).list() ){
            print("\n$dataFile")
        }
        return File(getDataPath(false)).list().size
    }
}
