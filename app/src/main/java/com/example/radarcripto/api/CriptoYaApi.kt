 // Esta interfaz se conecta a la API pública de CriptoYa
package com.example.radarcripto.api

import retrofit2.http.GET
import retrofit2.http.Path


 interface CriptoYaApi {

     @GET("{moneda}/ars")
     suspend fun obtenerPrecios(@Path("moneda") moneda: String): Map<String, ExchangeResponse>
 }
