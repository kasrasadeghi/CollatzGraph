package io.kaz.app

import io.kaz.view.GraphView
import javafx.application.Application.launch
import tornadofx.*

fun main(args: Array<String>) {
  launch(MyApp::class.java)
}

class MyApp: App(GraphView::class, Styles::class)