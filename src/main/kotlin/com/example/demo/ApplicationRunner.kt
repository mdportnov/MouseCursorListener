package com.example.demo

import com.example.demo.app.MyApp

class ApplicationRunner {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            MyApp().main(arrayOf())
        }
    }
}