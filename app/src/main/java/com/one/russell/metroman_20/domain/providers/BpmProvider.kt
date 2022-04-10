package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore

class BpmProvider(
    private val dataStore: PreferencesDataStore
) {

    val bpm = dataStore.bpm.getValueFlow()

    suspend fun setBpm(bpm: Int) {
        dataStore.bpm.setValue(bpm)
    }
}