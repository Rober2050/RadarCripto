package com.example.radarcripto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.radarcripto.navigation.AppNavGraph
import com.example.radarcripto.ui.theme.RadarCriptoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadarCriptoTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}