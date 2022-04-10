package com.one.russell.metroman_20.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PrefsValue<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) {

    fun getValueFlow(): Flow<T> {
        return dataStore.data
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    suspend fun getValue(): T {
        return dataStore.data
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
            .first()
    }

    suspend fun setValue(value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}