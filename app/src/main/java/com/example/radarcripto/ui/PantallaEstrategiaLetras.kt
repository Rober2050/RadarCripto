 package com.example.radarcripto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.radarcripto.ui.theme.RadarCriptoTheme

@Composable
fun EstrategiaLetrasDelTesoro() {
    RadarCriptoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📈 Estrategia: Letras del Tesoro (LECAPS / LEDES)",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Próxima licitación oficial: 26/04/2025",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Tasa Nominal Anual estimada: 72%",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Ganancia estimada: +5.6% en 30 días",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "¿Conviene hoy? ✔️ Sí, supera al rendimiento promedio de plazos fijos y staking USDT.",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(onClick = { /* TODO: abrir pantalla con tutorial paso a paso */ }) {
                Text("Ver cómo invertir paso a paso")
            }

            Text(
                text = "Fuente: Ministerio de Economía y brokers regulados. Se actualiza según licitaciones oficiales.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
