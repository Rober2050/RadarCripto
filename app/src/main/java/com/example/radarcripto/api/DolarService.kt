 package com.example.radarcripto.api

import com.example.radarcripto.model.DolarApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Interfaz que define la llamada a la API de dolarapi.com
interface DolarApi {
    @GET("dolares")
    suspend fun obtenerCotizaciones(): List<DolarApiResponse>
}

// Singleton que construye el cliente Retrofit
object DolarService {
    private const val BASE_URL = "https://dolarapi.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: DolarApi by lazy {
        retrofit.create(DolarApi::class.java)
    }
}
