package com.example.complex

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Div

/**
 * This application deliberately uses unchanged Android imports. It is compiled to a browser PWA.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("portableDroid", "Booting unchanged Android source in a PWA")
        setContent { CounterScreen() }
    }
}

@Composable
private fun CounterScreen() {
    var count by mutableStateOf(0)
    Div(attrs = { classes("screen") }) {
        TextView("portableDroid", Color.rgb(25, 118, 210))
        TextView("Une Activity Android avec imports android.* rendue par Compose Web.")
        Button("Compteur : $count") { count++ }
    }
}

fun main() {
    MainActivity().onCreate(null)
    portable.runtime.PortableRuntime.launch()
}
