package com.example.deltatomato

import androidx.compose.runtime.MutableState

data class Value(
    val x: String,
    val y: String,
    val z: String,
    val correct: String,
    val unCorrect: String,
    val percentage: String,
    val SM: Float =  6f,
    val SCB:Float = 3f
    )