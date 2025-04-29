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
        startDestination = "home"
    ) {
        composable("home") {
            PantallaHome(navController)
        }
        composable("comparador") {
            PantallaComparadorEstrategias(navController)
        }


        composable("simulador") {
            PantallaSimuladorRoi(navController)
        }
        composable("configuracion") {
            PantallaConfiguracion(navController)
        }
    }
}
