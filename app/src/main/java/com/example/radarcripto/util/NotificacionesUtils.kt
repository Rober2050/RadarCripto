package com.example.radarcripto.util

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.radarcripto.R

object NotificacionesUtils {

    private const val CHANNEL_ID = "roi_channel"
    private const val CHANNEL_NAME = "Alertas de Oportunidad de Inversión"
    private const val CHANNEL_DESCRIPTION = "Notificaciones cuando el ROI supera el objetivo configurado."
    private const val NOTIFICATION_ID = 1001
    private const val PREFS_NAME = "RadarCriptoPrefs"
    private const val ROI_KEY = "roi_objetivo"

    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)

    fun mostrarNotificacion(context: Context, mensaje: String) {
        crearCanal(context)

        // Verificar permiso para notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Pedir permiso si es una Activity
                if (context is Activity) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        100
                    )
                }
                return // No podemos enviar la notificación sin permiso
            }
        }

        // Construir la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("RadarCripto")
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Enviar la notificación

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())

    }

    private fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun guardarRoiObjetivo(context: Context, valor: Double) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(ROI_KEY, valor.toFloat()).apply()
    }

    fun obtenerRoiObjetivo(context: Context): Double {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(ROI_KEY, 1.5f).toDouble()
    }
}
