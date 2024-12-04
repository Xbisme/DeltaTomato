package com.example.deltatomato

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

@Composable
fun SpeedChange(viewModel: ValueViewModel) {
    val database = Firebase.database
    val databaseReference = database.getReference("speed")
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SpeedItem(title = "Speed of Motor", focusManager = focusManager,databaseReference, viewModel)
        SpeedItem(title = "Speed of Conveyor Belt", focusManager = focusManager,databaseReference, viewModel )
    }
}

@Composable
fun SpeedItem(title: String, focusManager: FocusManager, databaseReference: DatabaseReference, viewModel: ValueViewModel ) {

    var speed_fl by remember {
        mutableStateOf(if (title == "Speed of Motor") viewModel.value.SM else viewModel.value.SCB )

    }
        databaseReference.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (title == "Speed of Motor") {
                speed_fl = snapshot.child("SM").getValue<String>()?.toFloat() ?: 6f
                viewModel.onValueChanged(viewModel.value.copy(SM=speed_fl))
            }
            else {
                speed_fl = snapshot.child("SC").getValue<String>()?.toFloat() ?: 3f
                viewModel.onValueChanged(viewModel.value.copy(SCB = speed_fl))
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
        }
    })
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "$title: $speed_fl", color = Color.Black)
            Slider(
                value = speed_fl,
                onValueChange = {
                    speed_fl = it
                    if(title == "Speed of Motor") {
                        databaseReference.child("SM").setValue(it.toString())
                        databaseReference.child("SC").setValue((it/2).toString())
                    }
                    else {
                        databaseReference.child("SC").setValue(it.toString())
                        databaseReference.child("SM").setValue((it*2).toString())
                    }},
                valueRange =
                if(title == "Speed of Motor")0f..6f
                else 0f .. 3f,
                onValueChangeFinished = { focusManager.clearFocus() },
                colors = SliderDefaults.colors(
                    thumbColor = Color.Red,
                    activeTrackColor = Color.Blue
                ),
                modifier = Modifier.padding(horizontal = 8.dp),

            )
        }
    }
}
