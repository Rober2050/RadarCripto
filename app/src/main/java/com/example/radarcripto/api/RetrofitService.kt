package com.example.radarcripto.api

import com.example.radarcripto.model.DolarData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val BASE_URL = "https://criptoya.com/api/"

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .header("User-Agent", "RadarCriptoApp/1.0")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CriptoYaApi by lazy {
        retrofit.create(CriptoYaApi::class.java) // ✅ Usamos la interface de CriptoYaApi.kt
    }

    suspend fun obtenerPreciosDolar(): DolarData {
        return api.obtenerPreciosDolar()
    }
}
