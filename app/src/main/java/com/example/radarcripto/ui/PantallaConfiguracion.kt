package com.example.radarcripto.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.radarcripto.datastore.DataStoreManager
import kotlinx.coroutines.launch
//  import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults


@Composable
fun PantallaConfiguracion(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var roiObjetivo by remember { mutableStateOf("") }
    var tiempoConsulta by remember { mutableStateOf("") }

    val roiFlow = DataStoreManager.getRoiObjetivo(context).collectAsState(initial = 0.3)
    val tiempoFlow = DataStoreManager.getTiempoConsulta(context).collectAsState(initial = 15)

    LaunchedEffect(roiFlow.value, tiempoFlow.value) {
        if (roiObjetivo.isEmpty()) roiObjetivo = roiFlow.value.toString()
        if (tiempoConsulta.isEmpty()) tiempoConsulta = tiempoFlow.value.toString()
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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Configuración",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFD0D0FF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    OutlinedTextField(
                        value = tiempoConsulta,
                        onValueChange = { tiempoConsulta = it },
                        label = { Text("Tiempo de Consulta (minutos)", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedBorderColor = Color(0xFF4D9BFF),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF4D9BFF),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    OutlinedTextField(
                        value = roiObjetivo,
                        onValueChange = { roiObjetivo = it },
                        label = { Text("ROI Objetivo (%)", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedBorderColor = Color(0xFF4D9BFF),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF4D9BFF),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ButtonGuardar(
                texto = "Guardar",
                onClick = {
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
                }
            )
        }
    }
}

@Composable
fun ButtonGuardar(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF9B4DFF), Color(0xFF4D9BFF))
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(6.dp)
    ) {
        Text(texto)
    }
}
