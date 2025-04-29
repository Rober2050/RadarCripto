package com.example.radarcripto.model

import kotlinx.serialization.Serializable

@Serializable
data class DolarApiResponse(
    val casa: String,                    // << necesario para filtrar "oficial" y "blue"
    val nombre: String? = null,
    val compra: Double,
    val venta: Double,
    val fechaActualizacion: String? = null
)
