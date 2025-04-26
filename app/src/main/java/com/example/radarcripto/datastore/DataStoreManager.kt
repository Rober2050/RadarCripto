
package com.example.radarcripto.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Esta extensión va afuera, está bien
private val Context.dataStore by preferencesDataStore(name = "app_preferences")

object DataStoreManager {
    // 🔥 Claves de preferencias
    private val ROI_OBJETIVO_KEY = floatPreferencesKey("roi_objetivo")
    private val TIEMPO_CONSULTA_KEY = intPreferencesKey("tiempo_consulta")

    // 🔥 Función para obtener el ROI objetivo
    fun getRoiObjetivo(context: Context): Flow<Double> {
        return context.dataStore.data.map { preferences ->
            preferences[ROI_OBJETIVO_KEY]?.toDouble() ?: 0.3
        }
    }

    // 🔥 Función para guardar el ROI objetivo
    suspend fun setRoiObjetivo(context: Context, value: Double) {
        context.dataStore.edit { preferences ->
            preferences[ROI_OBJETIVO_KEY] = value.toFloat()
        }
    }

    // 🔥 Función para obtener el tiempo de consulta
    fun getTiempoConsulta(context: Context): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[TIEMPO_CONSULTA_KEY] ?: 15
        }
    }

    // 🔥 Función para guardar el tiempo de consulta
    suspend fun setTiempoConsulta(context: Context, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[TIEMPO_CONSULTA_KEY] = value
        }
    }
}
