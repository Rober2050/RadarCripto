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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.DecimalFormat
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults

import com.example.radarcripto.ui.components.CustomOutlinedTextField

import androidx.compose.runtime.Composable
@Composable
fun PantallaComparadorEstrategias(navController: NavHostController) {
    var montoUSD by remember { mutableStateOf("1000") }
    var tasaStaking by remember { mutableStateOf("9.0") }
    var tasaCarry by remember { mutableStateOf("38.0") }
    var plazoDias by remember { mutableStateOf("30") }
    var tipoCambioHoy by remember { mutableStateOf("1000.0") }
    var tipoCambioFuturo by remember { mutableStateOf("1000.0") }

    val formatter = DecimalFormat("#.##")
    val scrollState = rememberScrollState()

    val monto = montoUSD.toDoubleOrNull() ?: 0.0
    val tasaAnualStaking = tasaStaking.toDoubleOrNull() ?: 0.0
    val tasaAnualCarry = tasaCarry.toDoubleOrNull() ?: 0.0
    val dias = plazoDias.toIntOrNull() ?: 30
    val tcHoy = tipoCambioHoy.toDoubleOrNull() ?: 0.0
    val tcFuturo = tipoCambioFuturo.toDoubleOrNull() ?: 0.0

    val gananciaStaking = monto * (tasaAnualStaking / 100) * (dias / 365.0)
    val arsInvertidos = monto * tcHoy
    val arsFinal = arsInvertidos * (1 + (tasaAnualCarry / 100) * (dias / 365.0))
    val usdFinalCarry = if (tcFuturo > 0) arsFinal / tcFuturo else 0.0
    val gananciaCarry = usdFinalCarry - monto

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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp)) // ✅ Bajamos el título

            Text(
                text = "Comparar Estrategias",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFD0D0FF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomOutlinedTextField(montoUSD, { montoUSD = it }, "Monto en USD")
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomOutlinedTextField(tasaStaking, { tasaStaking = it }, "Staking USDT (APY%)", Modifier.weight(1f))
                        CustomOutlinedTextField(tasaCarry, { tasaCarry = it }, "Plazo Fijo ARS (TNA%)", Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomOutlinedTextField(plazoDias, { plazoDias = it }, "Días", Modifier.weight(1f))
                        CustomOutlinedTextField(tipoCambioHoy, { tipoCambioHoy = it }, "TC Actual", Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomOutlinedTextField(tipoCambioFuturo, { tipoCambioFuturo = it }, "TC Futuro Estimado")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔐 Staking USDT → Ganancia: ${formatter.format(gananciaStaking)} USDT (${formatter.format((gananciaStaking / monto) * 100)}%)",
                        color = Color.Cyan
                    )
                    Text(
                        text = "💱 Carry Trade → Ganancia: ${formatter.format(gananciaCarry)} USD (${formatter.format((gananciaCarry / monto) * 100)}%)",
                        color = if (gananciaCarry > gananciaStaking) Color.Green else Color.Cyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (gananciaCarry > gananciaStaking) {
                Text(
                    text = "✅ Te conviene hacer CARRY TRADE",
                    color = Color.Green,
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                Text(
                    text = "✅ Te conviene hacer STAKING EN USDT",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}




@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
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







