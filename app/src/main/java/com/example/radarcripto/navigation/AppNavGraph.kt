package com.example.radarcripto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.radarcripto.ui.*


@Composable

fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "estrategiaDia"
    ) {
        composable("estrategiaDia") {
            PantallaEstrategiaDelDia(navController)
        }
        composable("home") {
            PantallaHome(navController)
        }
        composable("simulador") {
            PantallaSimuladorRoi()
        }
        composable("comparador") {
            PantallaComparadorEstrategias()
        }
        composable("configuracion") {
            PantallaConfiguracion(navController)
        }
    }
}
