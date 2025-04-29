 package com.example.radarcripto.model

data class DolarData(
    val oficial: DolarInfo,
    val blue: DolarInfo
)

data class DolarInfo(
    val price: Double
)
