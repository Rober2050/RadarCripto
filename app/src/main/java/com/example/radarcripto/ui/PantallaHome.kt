package com.example.radarcripto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.radarcripto.ui.components.estiloBotonPrincipalOscuro

@Composable
fun PantallaHome(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("RadarCripto", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("simulador") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = estiloBotonPrincipalOscuro()
        ) {
            Text("Simulador de ROI")
        }

        Button(
            onClick = { navController.navigate("comparador") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = estiloBotonPrincipalOscuro()
        ) {
            Text("Comparar Estrategias")
        }

        Button(
            onClick = { navController.navigate("configuracion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = estiloBotonPrincipalOscuro()
        ) {
            Text("Configuración")
        }
    }
}