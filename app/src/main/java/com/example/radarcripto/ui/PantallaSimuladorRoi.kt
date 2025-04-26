

// Archivo: PantallaSimuladorRoi.kt
// Propósito: Simulador de ROI + Comparador entre exchanges + Comparador contra dólar blue
// Autor: RadarCripto Team
// Última modificación: integración con https://dolarapi.com + manejo de errores

package com.example.radarcripto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.radarcripto.api.RetrofitService
import com.example.radarcripto.datastore.DataStoreManager
import com.example.radarcripto.util.NotificacionesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Locale
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject
import com.example.radarcripto.ui.components.estiloTextFieldOscuro
import androidx.compose.ui.tooling.preview.Preview
import com.example.radarcripto.ui.theme.RadarCriptoTheme

enum class StableCoin {
    USDT, USDC, DAI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable



fun PantallaSimuladorRoi() {


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var roiObjetivo by remember { mutableStateOf(0.3) }
    var precioCompra by remember { mutableStateOf("") }
    var precioVenta by remember { mutableStateOf("") }
    var resultadoROI by remember { mutableStateOf("") }
    var oportunidadInversion by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var montoInversion by remember { mutableStateOf("1000") }
    var gananciaEnDinero by remember { mutableStateOf("") }
    var monedaSeleccionada by remember { mutableStateOf(StableCoin.USDT) }

    val cotizaciones = remember { mutableStateMapOf<String, Pair<Double, Double>>() }
    val scrollState = rememberScrollState()

    // Lista de exchanges integrados vía CriptoYa
    val exchanges = listOf(
        "belo",           // ya integrado
        "buenbit",        // ya integrado
        "lemoncash",      // ya integrado
        "ripio",          // ya integrado
        "satoshitango",   // ya integrado
        "binance",        // NUEVO: integración agregada
        "bitso",          // NUEVO: integración agregada
        //"bingx" sin cotizacion por ahora           // NUEVO: P2P, integración agregada
    )



    LaunchedEffect(Unit) {
        DataStoreManager.getRoiObjetivo(context).collectLatest {
            roiObjetivo = it
        }
    }

    LaunchedEffect(monedaSeleccionada) {
        isLoading = true
        try {
            val todasLasRespuestas = withContext(Dispatchers.IO) {
                RetrofitService.api.obtenerPrecios(monedaSeleccionada.name.lowercase())
            }
            cotizaciones.clear()
            for (exchange in exchanges) {
                val respuesta = todasLasRespuestas[exchange]
                if (respuesta != null) {
                    cotizaciones[exchange] = Pair(respuesta.totalBid, respuesta.totalAsk)
                }
            }
            val mejorCompraEntry = cotizaciones.minByOrNull { it.value.second }  // Menor ask (compra)
            val mejorVentaEntry = cotizaciones.maxByOrNull { it.value.first }   // Mayor bid (venta)

            if (mejorCompraEntry != null && mejorVentaEntry != null) {
                val mejorCompra = mejorCompraEntry.value.second
                val mejorVenta = mejorVentaEntry.value.first
                precioCompra = mejorCompra.toString()
                precioVenta = mejorVenta.toString()
            }
        } catch (e: Exception) {
            cotizaciones.clear()
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simulador de ROI", style = MaterialTheme.typography.titleLarge, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            StableCoin.values().forEach { moneda ->
                FilterChip(
                    selected = monedaSeleccionada == moneda,
                    onClick = { monedaSeleccionada = moneda },
                    label = { Text(moneda.name) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = FilterChipDefaults.filterChipColors()
                )

            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("ROI objetivo actual: $roiObjetivo%", color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precioCompra,
            onValueChange = { precioCompra = it },
            label = { Text("Compra", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp), // 🔽 altura mínima más compacta
            minLines = 1,
            singleLine = true,
            colors =  estiloTextFieldOscuro()


    )


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precioVenta,
            onValueChange = { precioVenta = it },
            label = { Text("Venta", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                     .fillMaxWidth()
                     .heightIn(min = 40.dp), // 🔽 altura mínima más compacta
            minLines = 1,
            singleLine = true,
            colors =  estiloTextFieldOscuro()
    )


        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = montoInversion,
            onValueChange = { montoInversion = it },
            label = { Text("Monto a invertir (${monedaSeleccionada.name})", color = Color.White) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp), // 🔽 altura mínima más compacta
            minLines = 1,
            singleLine = true,
            colors =  estiloTextFieldOscuro()
            )

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                val compra = precioCompra.toDoubleOrNull()
                val venta = precioVenta.toDoubleOrNull()

                if (compra != null && venta != null && compra > 0) {
                    val roi = ((venta - compra) / compra) * 100
                    val formatter = DecimalFormat("#.##")
                    resultadoROI = "ROI: ${formatter.format(roi)}%"
                    oportunidadInversion = roi >= roiObjetivo

                    if (oportunidadInversion) {
                        NotificacionesUtils.mostrarNotificacion(
                            context,
                            "¡ROI alcanzado del ${formatter.format(roi)}%! Posible oportunidad."
                        )
                        montoInversion.toDoubleOrNull()?.let {
                            val ganancia = (roi / 100) * it
                            gananciaEnDinero = "Ganancia estimada: ${formatter.format(ganancia)} ${monedaSeleccionada.name}"
                        }
                    } else {
                        gananciaEnDinero = ""
                    }
                } else {
                    resultadoROI = "Valores inválidos"
                    oportunidadInversion = false
                    gananciaEnDinero = ""
                }
            },
            enabled = !isLoading
        ) {
            Text("Calcular")
        }

        // -----------------------------
// Sección: ¿Dónde conviene comprar y vender hoy?
// -----------------------------
        Spacer(modifier = Modifier.height(12.dp))
        Text("¿Dónde conviene comprar y vender hoy?", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

// Detectar exchange más barato (menor ASK) y más caro (mayor BID)
        val mejorCompraEntry = cotizaciones.minByOrNull { it.value.second }
        val mejorVentaEntry = cotizaciones.maxByOrNull { it.value.first }

        if (mejorCompraEntry != null && mejorVentaEntry != null) {
            val compra = mejorCompraEntry.value.second
            val venta = mejorVentaEntry.value.first
            val roi = if (compra > 0) ((venta - compra) / compra) * 100 else 0.0
            val monto = montoInversion.toDoubleOrNull() ?: 0.0
            val ganancia = (roi / 100) * monto
            val formatter = DecimalFormat("#.##")

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("🛒 Comprar en: ${mejorCompraEntry.key.capitalize()} → $compra", color = Color.White)
                Text("💰 Vender en: ${mejorVentaEntry.key.capitalize()} → $venta", color = Color.White)
                Text("📈 ROI estimado: ${formatter.format(roi)}%", color = if (roi > 0) Color.Green else Color.Red)
                if (roi > 0) {
                    Text("✅ Ganancia estimada: ${formatter.format(ganancia)} ${monedaSeleccionada.name}", color = Color(0xFF66BB6A))
                } else {
                    Text("❌ No rentable actualmente", color = Color.Red)
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        Text(resultadoROI, color = Color.White)
        if (resultadoROI.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            if (oportunidadInversion) {
                Text("✅ Oportunidad de inversión alineada al ROI: $resultadoROI", color = Color(0xFF66BB6A))
                if (gananciaEnDinero.isNotBlank()) {
                    Text(gananciaEnDinero, color = Color.White)
                }
            } else {
                Text("❌ No hay oportunidad de inversión", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Cotizaciones ${monedaSeleccionada.name}/ARS (compra)", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))




        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Exchange", color = Color.LightGray)
                Text("Precio", color = Color.LightGray)
            }
            cotizaciones.forEach { (exchange, precios) ->
                val color = if (precios.second == cotizaciones.values.minOfOrNull { it.second }) Color.Green else Color.White
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    val tipo = if (exchange == "bingx") " (P2P)" else " (Exchange)"
                    Text(exchange.replaceFirstChar { it.uppercase() } + tipo, color = color)

                    Text(String.format("%.4f", precios.second), color = color)
                }
            }
        }

        // Espaciado visual antes de mostrar la tabla de precios de venta
        Spacer(modifier = Modifier.height(12.dp))

// Título para la sección de cotizaciones de venta (totalBid)
        Text(
            "Cotizaciones ${monedaSeleccionada.name}/ARS (venta)",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

// Contenedor general para la tabla de cotizaciones de venta
        Column(modifier = Modifier.fillMaxWidth()) {

            // Encabezado de la tabla: nombres de columnas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exchange", color = Color.LightGray) // Columna izquierda: nombre del exchange
                Text("Precio", color = Color.LightGray)   // Columna derecha: precio de venta
            }

            // Recorremos el mapa de cotizaciones para mostrar los valores
            cotizaciones.forEach { (exchange, precios) ->

                // Si este precio de venta (bid) es el mayor, se resalta en celeste
                val color = if (precios.first == cotizaciones.values.maxOfOrNull { it.first }) Color.Cyan else Color.White

                // Fila con nombre del exchange y su respectivo precio de venta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Nombre del exchange con la primera letra en mayúscula
                    Text(exchange.replaceFirstChar { it.uppercase() }, color = color)

                    // Mostramos el totalBid (precio de venta real)
                    Text(String.format(Locale.US, "%.4f", precios.first), color = color)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // -----------------------------
// Sección: Comparación con Dólar Blue
// -----------------------------
        Spacer(modifier = Modifier.height(12.dp))
        Text("Comparación con el Dólar Blue", style = MaterialTheme.typography.titleMedium, color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        val blueDolar = remember { mutableStateOf(0.0) }

// Integración actualizada del dólar blue desde dolarapi.com con manejo de errores
// Reemplaza la API de Dolarsi y evita mostrar un número por defecto si falla

// Variables de estado

        val blueDisponible = remember { mutableStateOf(true) }

// Llamada HTTP segura
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                try {
                    val url = URL("https://dolarapi.com/v1/dolares/blue")
                    val conn = url.openConnection() as HttpURLConnection
                    val response = conn.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(response)
                    val venta = jsonObject.getDouble("venta")
                    if (venta > 0) {
                        blueDolar.value = venta
                        blueDisponible.value = true
                    } else {
                        blueDisponible.value = false
                    }
                } catch (e: Exception) {
                    blueDisponible.value = false
                }
            }
        }


        // Mostrar resultado si hay cotización de blue y un exchange para comparar
        // En el bloque visual:
        if (blueDisponible.value && mejorCompraEntry != null) {
            val precioCripto = mejorCompraEntry.value.second
            val roiVsBlue = ((blueDolar.value - precioCripto) / precioCripto) * 100
            val formatter = DecimalFormat("#.##")
            val monto = montoInversion.toDoubleOrNull() ?: 0.0
            val ganancia = (roiVsBlue / 100) * monto

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("💵 Dólar Blue actual: ${blueDolar.value}", color = Color.White)
                Text("🛒 Mejor precio USDT (para comprar): ${mejorCompraEntry.key.capitalize()} → $precioCripto", color = Color.White)
                Text("📈 ROI contra dólar blue: ${formatter.format(roiVsBlue)}%", color = if (roiVsBlue > 0) Color.Green else Color.Red)
                if (roiVsBlue > 0) {
                    Text("✅ Ganancia estimada vendiendo al blue: ${formatter.format(ganancia)} ARS", color = Color(0xFF66BB6A))
                } else {
                    Text("❌ No rentable frente al blue actualmente", color = Color.Red)
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("⚠️ No se pudo obtener la cotización del dólar blue o el mejor precio de compra aún no está disponible.", color = Color.Gray)
                Text("🛠 Debug → Blue: ${blueDolar.value}, Compra: ${mejorCompraEntry?.value?.second ?: "null"}", color = Color.DarkGray)
            }
        }

    }
}



// Para previsualizar la UX
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaComparador() {
    RadarCriptoTheme {
        PantallaSimuladorRoi()
    }
}
