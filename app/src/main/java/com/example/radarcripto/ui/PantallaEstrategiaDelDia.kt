package com.example.radarcripto.ui

import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.Serializable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.radarcripto.api.LecapData
import com.example.radarcripto.api.obtenerLecapDelDiaOConFallback
import com.example.radarcripto.ui.theme.RadarCriptoTheme

@Composable
fun PantallaEstrategiaDelDia(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var lecap by remember { mutableStateOf<LecapData?>(null) }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            lecap = obtenerLecapDelDiaOConFallback(context)
        } catch (e: Exception) {
            error = true
        }
    }

    RadarCriptoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (error) {
                Text("Error al obtener datos de LECAP", color = Color.Red)
            } else if (lecap == null) {
                CircularProgressIndicator()
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "✅ Te conviene invertir en ${lecap!!.letra}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Green
                        )

                        Text("Plazo: ${lecap!!.plazo}")
                        Text("TNA estimada: ${lecap!!.tna}")
                        Text("Ganancia estimada: ${lecap!!.ganancia_estimada}", color = Color.Green)
                        Text("Vencimiento: ${lecap!!.vencimiento}")

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Text("📌 Motivos para invertir hoy:")
                        Text("✔️ Supera a plazo fijo y staking")
                        Text("✔️ Alta demanda en licitaciones previas")
                        Text("✔️ Plazo corto y bajo riesgo")

                        Button(onClick = { /* TODO: acción tutorial */ }, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Filled.AttachMoney, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Ver cómo invertir paso a paso")
                        }

                        Text(
                            text = "Fuente: ${lecap!!.fuente}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3949AB),
                        contentColor = Color.White
                    )
                ) {
                    Text("Ir al menú principal")
                }
            }
        }
    }
}
