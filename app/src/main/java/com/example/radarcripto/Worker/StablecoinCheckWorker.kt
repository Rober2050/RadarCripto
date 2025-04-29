package com.example.radarcripto.worker

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.radarcripto.api.DolarService
import com.example.radarcripto.datastore.DataStoreManager
import com.example.radarcripto.util.NotificacionesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class StablecoinCheckWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            val roiObjetivo = DataStoreManager.getRoiObjetivo(context).first()

            val lista = withContext(Dispatchers.IO) {
                DolarService.api.obtenerCotizaciones()
            }

            val oficial = lista.firstOrNull { it.casa == "oficial" }
            val blue = lista.firstOrNull { it.casa == "blue" }

            if (oficial == null || blue == null) {
                Log.e("StablecoinWorker", "No se encontraron cotizaciones válidas")
                return Result.failure()
            }

            val precioCompra = oficial.compra
            val precioVenta = blue.venta

            if (precioCompra > 0 && precioVenta > 0) {
                val roi = ((precioVenta - precioCompra) / precioCompra) * 100
                val formatter = DecimalFormat("#.##")

                if (roi > roiObjetivo) {
                    NotificacionesUtils.mostrarNotificacion(
                        context,
                        "Oportunidad detectada: ROI del ${formatter.format(roi)}%"
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("StablecoinWorker", "Error ejecutando worker", e)
            Result.failure()
        }
    }
}
