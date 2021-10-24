package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore

class SaveCurrentBpmUseCase(
    private val preferencesDataStore: PreferencesDataStore
) {
    suspend fun execute(bpm: Int) = preferencesDataStore.saveBpm(bpm)
}