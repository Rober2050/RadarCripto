package com.example.radarcripto.ui


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.radarcripto.datastore.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun PantallaConfiguracion(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var roiObjetivo by remember { mutableStateOf("") }
    var tiempoConsulta by remember { mutableStateOf("") }

    // Cargar valores actuales desde DataStore
    val roiFlow = DataStoreManager.getRoiObjetivo(context).collectAsState(initial = 0.3)
    val tiempoFlow = DataStoreManager.getTiempoConsulta(context).collectAsState(initial = 15)

    // Inicializar campos si están vacíos
    LaunchedEffect(roiFlow.value, tiempoFlow.value) {
        if (roiObjetivo.isEmpty()) roiObjetivo = roiFlow.value.toString()
        if (tiempoConsulta.isEmpty()) tiempoConsulta = tiempoFlow.value.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Configuración", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = roiObjetivo,
            onValueChange = { roiObjetivo = it },
            label = { Text("ROI Objetivo (%)", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor =  Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = tiempoConsulta,
            onValueChange = { tiempoConsulta = it },
            label = { Text("Tiempo de Consulta (minutos)", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            scope.launch {
                val roiParsed = roiObjetivo.toDoubleOrNull()
                val tiempoParsed = tiempoConsulta.toIntOrNull()
                if (roiParsed != null && tiempoParsed != null) {
                    DataStoreManager.setRoiObjetivo(context, roiParsed)
                    DataStoreManager.setTiempoConsulta(context, tiempoParsed)
                    Toast.makeText(context, "Configuración guardada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Valores inválidos", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Guardar")
        }
    }
}
