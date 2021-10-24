package com.one.russell.metroman_20.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PreferencesDataStore.DATASTORE_NAME
)

@Suppress("PrivatePropertyName")
class PreferencesDataStore(
    private val appContext: Context
) {

    private val KEY_BPM = intPreferencesKey("KEY_BPM")

    private val DEFAULT_BPM = 60

    suspend fun getBpm(): Int {
        return appContext.dataStore.data
            .map { preferences ->
                preferences[KEY_BPM] ?: DEFAULT_BPM
            }
            .first()
    }

    suspend fun saveBpm(bpm: Int) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_BPM] = bpm
        }
    }

    companion object {
        const val DATASTORE_NAME = "data_store"
    }
}

