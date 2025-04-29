
package com.example.radarcripto.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.radarcripto.api.DolarService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.TextFieldDefaults
import com.example.radarcripto.model.DolarApiResponse

@Composable
fun PantallaSimuladorRoi(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var precioCompraOficial by remember { mutableStateOf("") }
    var precioVentaBlue by remember { mutableStateOf("") }
    var montoInversion by remember { mutableStateOf("1000") }

    var cotizacionOficial by remember { mutableStateOf<Double?>(null) }
    var cotizacionBlue by remember { mutableStateOf<Double?>(null) }
    var errorCarga by remember { mutableStateOf(false) }

    var roiCalculado by remember { mutableStateOf<Double?>(null) }
    var gananciaCalculada by remember { mutableStateOf<Double?>(null) }
    var mensajeOportunidad by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    fun calcularAutomaticamente() {
        val compra = precioCompraOficial.toDoubleOrNull() ?: 0.0
        val venta = precioVentaBlue.toDoubleOrNull() ?: 0.0
        val monto = montoInversion.toDoubleOrNull() ?: 0.0

        if (compra > 0 && venta > 0 && monto > 0) {
            val roi = ((venta - compra) / compra) * 100
            val ganancia = (venta - compra) * (monto / compra)

            roiCalculado = roi
            gananciaCalculada = ganancia
            mensajeOportunidad = if (roi > 0) "✅ Oportunidad: Conviene comprar Oficial y vender Blue." else "Hoy no hay diferencia significativa."
        } else {
            mensajeOportunidad = "❌ Ingresá datos válidos para calcular."
        }
    }

    LaunchedEffect(Unit) {
        try {

            val responseList: List<DolarApiResponse> = DolarService.api.obtenerCotizaciones()
            val precios: DolarApiResponse = responseList.first()

            val lista: List<DolarApiResponse> = DolarService.api.obtenerCotizaciones()

            val oficial = lista.firstOrNull { it.casa == "oficial" }?.venta ?: 0.0
            val blue = lista.firstOrNull { it.casa == "blue" }?.venta ?: 0.0


            if (oficial > 0 && blue > 0) {
                cotizacionOficial = oficial
                cotizacionBlue = blue

                precioCompraOficial = oficial.toInt().toString()
                precioVentaBlue = blue.toInt().toString()

                calcularAutomaticamente()
                errorCarga = false
            } else {
                Log.e("PantallaSimulador", "Cotizaciones inválidas: oficial=$oficial, blue=$blue")
                errorCarga = true
            }
        } catch (e: Exception) {
            Log.e("PantallaSimulador", "Error cargando datos: ${e.localizedMessage}", e)
            errorCarga = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0E25), Color(0xFF0B0F2D))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Simulador de ROI", style = MaterialTheme.typography.headlineMedium, color = Color(0xFFD0D0FF))

            when {
                errorCarga -> Text("❌ Error al cargar precios. Intenta nuevamente.", color = Color.Red)
                cotizacionOficial == null || cotizacionBlue == null -> Text("Cargando cotizaciones...", color = Color.LightGray)
                else -> Text("Oficial: \$${cotizacionOficial?.toInt()} - Blue: \$${cotizacionBlue?.toInt()}", color = Color.White)
            }

            listOf(
                Triple(precioCompraOficial, { valor: String -> precioCompraOficial = valor }, "Precio Compra Oficial"),
                Triple(precioVentaBlue, { valor: String -> precioVentaBlue = valor }, "Precio Venta Blue"),
                Triple(montoInversion, { valor: String -> montoInversion = valor }, "Monto a Invertir en Pesos")
            ).forEach { (value, setter, label) ->

                OutlinedTextField(
                    value = value,
                    onValueChange = setter,
                    label = { Text(label, color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color(0xFF4D9BFF),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color(0xFF4D9BFF),
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(600)
                        calcularAutomaticamente()
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D9BFF), contentColor = Color.White),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Calcular")
            }

            roiCalculado?.let {
                Text("ROI estimado: ${"%.2f".format(it)}%", color = if (it > 0) Color.Green else Color.Gray)
            }

            gananciaCalculada?.let {
                Text("Ganancia estimada: \$${"%.2f".format(it)}", color = if (it > 0) Color.Green else Color.Gray)
            }

            if (mensajeOportunidad.isNotEmpty()) {
                Text(text = mensajeOportunidad, color = if (roiCalculado != null && roiCalculado!! > 0) Color.Green else Color.Gray)
            }
        }
    }
}
