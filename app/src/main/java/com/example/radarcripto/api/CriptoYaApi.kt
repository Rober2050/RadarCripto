 // Esta interfaz se conecta a la API pública de CriptoYa
package com.example.radarcripto.api


import com.example.radarcripto.model.Cotizacion
import com.example.radarcripto.model.DolarData
import retrofit2.http.GET
import retrofit2.http.Path


interface CriptoYaApi {

        @GET("dolar")
        suspend fun obtenerPreciosDolar(): DolarData
        @GET("{stablecoin}/p2p")
         suspend fun obtenerPrecios(@Path("stablecoin") stablecoin: String): Map<String, Cotizacion>

}

