 
 package com.example.radarcripto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaHome(navController: NavController) {
    var botonSeleccionado by remember { mutableStateOf("") }

    // Definimos el fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E25), // azul oscuro arriba
                        Color(0xFF0B0F2D)  // azul un poquito más claro abajo
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "RadarCripto",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFFD0D0FF)
            )

            Spacer(modifier = Modifier.height(48.dp))

            BotonHome(
                texto = "Simulador de ROI",
                seleccionado = botonSeleccionado == "simulador",
                onClick = {
                    botonSeleccionado = "simulador"
                    navController.navigate("simulador")
                }
            )

            BotonHome(
                texto = "Comparar Estrategias",
                seleccionado = botonSeleccionado == "comparador",
                onClick = {
                    botonSeleccionado = "comparador"
                    navController.navigate("comparador")
                }
            )

            BotonHome(
                texto = "Configuración",
                seleccionado = botonSeleccionado == "configuracion",
                onClick = {
                    botonSeleccionado = "configuracion"
                    navController.navigate("configuracion")
                }
            )
        }
    }
}

@Composable
fun BotonHome(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    val fondo = if (seleccionado) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF9B4DFF), // violeta fuerte
                Color(0xFF4D9BFF)  // azul eléctrico
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF1E1E2E),
                Color(0xFF1E1E2E)
            )
        )
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(fondo, shape = RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(6.dp)
    ) {
        Text(text = texto)
    }
}
