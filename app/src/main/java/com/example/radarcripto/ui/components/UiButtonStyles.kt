package com.example.radarcripto.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun estiloBotonPrincipalOscuro() = ButtonDefaults.buttonColors(
    containerColor = Color(0xFF3949AB), // Azul cobalto
    contentColor = Color.White
)