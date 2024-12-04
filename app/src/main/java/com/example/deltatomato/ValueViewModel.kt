package com.example.deltatomato

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValueViewModel : ViewModel(){
    var value by mutableStateOf(Value(
        "",
        "",
        "",
        "0",
        "0",
        ""))
    fun onValueChanged(newValue: Value) {
        value = newValue
    }

}