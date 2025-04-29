package com.example.radarcripto.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue

@Composable
fun textFieldRadarCriptoColors(isFocused: Boolean = false): TextFieldColors {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF4D9BFF) else Color.Gray,
        label = "BorderColorFocusAnimation"
    )

    val animatedLabelColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF4D9BFF) else Color.Gray,
        label = "LabelColorFocusAnimation"
    )

    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        cursorColor = Color.White,
        focusedBorderColor = animatedBorderColor,
        unfocusedBorderColor = animatedBorderColor,
        focusedLabelColor = animatedLabelColor,
        unfocusedLabelColor = animatedLabelColor
    )
}
