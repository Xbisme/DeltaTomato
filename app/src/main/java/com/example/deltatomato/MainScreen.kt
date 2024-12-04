package com.example.deltatomato

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MainScreen(viewModel: ValueViewModel = viewModel()) {
    val database = Firebase.database
    val myRef = database.getReference("esp32Data/value")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Delta Tomato Robot",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        CoorResScreen(myRef, viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier
            .background(
                Color.White,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(30.dp)
        ){
            PieChart(viewModel)
        }
        Spacer(modifier = Modifier.height(20.dp))
        SpeedChange(viewModel)
    }
}
@Composable
fun CoorResScreen(databaseReference: DatabaseReference, viewModel: ValueViewModel) {

    databaseReference.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val x = snapshot.child("x").getValue<String>().toString()
            val y = snapshot.child("y").getValue<String>().toString()
            val z = snapshot.child("z").getValue<String>().toString()

            val correctValue = snapshot.child("dnum").getValue<String>()?.toFloatOrNull() ?: 0f
            val unCorrectValue = snapshot.child("snum").getValue<String>()?.toFloatOrNull() ?: 0f
            val correct = correctValue.toInt().toString()
            val unCorrect = unCorrectValue.toInt().toString()
            val percentage = if (unCorrectValue == 0f) {
                "100"
            } else {
                val percent = 100 * (correctValue / (correctValue + unCorrectValue))
                String.format("%.2f", percent)
            }
            val value = Value(x,y,z,correct,unCorrect, percentage)
            viewModel.onValueChanged(value)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ){
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .weight(1f)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Coordinate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Text(
                    text = "x: ${viewModel.value.x}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "y: ${viewModel.value.y}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "z: ${viewModel.value.z}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .weight(1f)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Result",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )

                Row( Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        Text(
                            text = "Correct:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = "UnCorrect:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = "Percentage:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    Column {
                        Text(
                            text = "${viewModel.value.correct}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = "${viewModel.value.unCorrect}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = "${viewModel.value.percentage}%",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
            }
        }
    }
}
