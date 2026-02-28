package br.com.manieri.amanitamuscaria

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AutoCheck Pro",
    ) {
        App()
    }
}
