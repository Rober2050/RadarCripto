 package com.example.radarcripto.model

import kotlinx.serialization.Serializable

@Serializable
data class Cotizacion(
    val totalAsk: Double = 0.0,
    val totalBid: Double = 0.0
)
