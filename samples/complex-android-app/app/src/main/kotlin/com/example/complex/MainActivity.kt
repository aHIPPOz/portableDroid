package com.example.complex

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * Fixture de contrat : ces imports doivent rester inchangés pour toutes les cibles.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("portableDroid", "Application source kept unchanged")
    }
}
