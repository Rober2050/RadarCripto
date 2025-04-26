package com.radarcripto.data

import androidx.room.*

@Entity
data class Oportunidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exchangeOrigen: String,
    val exchangeDestino: String,
    val roi: Double,
    val timestamp: Long
)

@Dao
interface OportunidadDao {
    @Insert
    suspend fun insertar(oportunidad: Oportunidad)

    @Query("SELECT * FROM Oportunidad ORDER BY timestamp DESC")
    suspend fun obtenerTodas(): List<Oportunidad>
}
