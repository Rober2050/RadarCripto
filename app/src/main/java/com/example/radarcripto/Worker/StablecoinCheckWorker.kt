
package com.example.radarcripto.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.radarcripto.api.RetrofitService
import com.example.radarcripto.datastore.DataStoreManager
import com.example.radarcripto.ui.StableCoin
import com.example.radarcripto.util.NotificacionesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import kotlinx.coroutines.flow.first


class StablecoinCheckWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
	
     
	    val roiObjetivo = DataStoreManager.getRoiObjetivo(context).first()


        try {
            for (moneda in StableCoin.values()) {
                val resultado = verificarMoneda(moneda, roiObjetivo)
                if (resultado != null) {
                    NotificacionesUtils.mostrarNotificacion(
                        context,
                        "Oportunidad en ${moneda.name}: ROI del ${resultado}%" 
                    )
                    return Result.success()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.success()
    }

    private suspend fun verificarMoneda(moneda: StableCoin, roiObjetivo: Double): String? {
        return withContext(Dispatchers.IO) {
            val cotizaciones = RetrofitService.api.obtenerPrecios(moneda.name.lowercase())

            val mejoresAsk = cotizaciones.values.map { it.totalAsk }.filter { it > 0 }
            val mejoresBid = cotizaciones.values.map { it.totalBid }.filter { it > 0 }

            if (mejoresAsk.isEmpty() || mejoresBid.isEmpty()) return@withContext null

            val mejorCompra = mejoresAsk.minOrNull() ?: return@withContext null
            val mejorVenta = mejoresBid.maxOrNull() ?: return@withContext null

            val roi = ((mejorVenta - mejorCompra) / mejorCompra) * 100
            val formatter = DecimalFormat("#.##")

            if (roi >= roiObjetivo) formatter.format(roi) else null
        }
    }
}
