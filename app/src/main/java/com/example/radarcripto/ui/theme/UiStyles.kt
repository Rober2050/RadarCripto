package com.example.radarcripto.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun estiloTextFieldOscuro() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedLabelColor = Color.LightGray,
    unfocusedLabelColor = Color.Gray,
    focusedBorderColor = Color.LightGray,
    unfocusedBorderColor = Color.Gray,
    disabledBorderColor = Color.DarkGray,
    errorBorderColor = Color.Red,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent
)