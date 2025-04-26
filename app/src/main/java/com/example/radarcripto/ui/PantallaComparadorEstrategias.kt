package com.example.radarcripto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import com.example.radarcripto.ui.theme.RadarCriptoTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Composable
fun PantallaComparadorEstrategias() {
    var montoUSD by remember { mutableStateOf("1000") }
    var tasaStaking by remember { mutableStateOf("9.0") }
    var tasaCarry by remember { mutableStateOf("38.0") }
    var plazoDias by remember { mutableStateOf("30") }
    var tipoCambioHoy by remember { mutableStateOf("1000.0") }
    var tipoCambioFuturo by remember { mutableStateOf("1000.0") }

    val formatter = DecimalFormat("#.##")
    val scroll = rememberScrollState()

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

    val fieldColors = OutlinedTextFieldDefaults.colors(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll)
            .background(Color(0xFF121212))
    ) {
        Text("¿Qué me conviene hoy?", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = montoUSD,
            onValueChange = { montoUSD = it },
            label = { Text("Monto en USD", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = tasaStaking,
                onValueChange = { tasaStaking = it },
                label = { Text("Tasa Staking USDT (APY %)", color = Color.White) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                colors = fieldColors
            )
            OutlinedTextField(
                value = tasaCarry,
                onValueChange = { tasaCarry = it },
                label = { Text("Tasa Plazo Fijo ARS (TNA %)", color = Color.White) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                colors = fieldColors
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = plazoDias,
                onValueChange = { plazoDias = it },
                label = { Text("Días", color = Color.White) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                colors = fieldColors
            )
            OutlinedTextField(
                value = tipoCambioHoy,
                onValueChange = { tipoCambioHoy = it },
                label = { Text("TC actual", color = Color.White) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                colors = fieldColors
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = tipoCambioFuturo,
            onValueChange = { tipoCambioFuturo = it },
            label = { Text("TC futuro estimado", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("🔐 Staking USDT → Ganancia: ${formatter.format(gananciaStaking)} USDT (${formatter.format((gananciaStaking / monto) * 100)}%)", color = Color.Cyan)
        Text("💱 Carry Trade → Ganancia: ${formatter.format(gananciaCarry)} USD (${formatter.format((gananciaCarry / monto) * 100)}%)", color = if (gananciaCarry > gananciaStaking) Color.Green else Color.Yellow)

        Spacer(modifier = Modifier.height(24.dp))
        if (gananciaCarry > gananciaStaking) {
            Text("✅ Te conviene hacer CARRY TRADE", color = Color.Green)
        } else {
            Text("✅ Te conviene hacer STAKING EN USDT", color = Color.Cyan)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaComparadorEstrategias() {
    RadarCriptoTheme {
        PantallaComparadorEstrategias()
    }
}