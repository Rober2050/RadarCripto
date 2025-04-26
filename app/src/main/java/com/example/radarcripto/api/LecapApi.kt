package com.example.radarcripto.api

import android.content.Context
import android.net.http.HttpResponseCache.install
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LecapData(
    val letra: String,
    val tna: String,
    val plazo: String,
    val ganancia_estimada: String,
    val vencimiento: String,
    val fecha_licitacion: String,
    val fuente: String
)

suspend fun obtenerLecapDelDiaOConFallback(context: Context): LecapData {
    return try {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val url = "https://usuario.github.io/radarcripto-datos/lecap_hoy.json"
        client.get(url).body()
    } catch (e: Exception) {
        leerLecapDesdeAssets(context)
    }
}

fun leerLecapDesdeAssets(context: Context): LecapData {
    val inputStream = context.assets.open("lecap_hoy.json")
    val json = inputStream.bufferedReader().use { it.readText() }
    return Json.decodeFromString(json)
}
